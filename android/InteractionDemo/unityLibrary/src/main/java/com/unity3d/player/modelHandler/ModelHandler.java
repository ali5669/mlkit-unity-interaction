package com.unity3d.player.modelHandler;

import android.content.Context;
import android.util.Log;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * A tool which gets specified pytorch model and retrieve skeletal data,
 * normalize the input data for the model and obtain the output.
 */
public class ModelHandler {
    public String name = "";

    public float[] buffer;
    private int len = 0;

    private final long max_length;

    public Module model;

    private static ModelHandler modelHandler;

    private Map<Integer, String> map;

    private ModelHandler(String name, int max_length, String model_path) {
        this.name = name;
        this.buffer = new float[max_length * 48];
        this.max_length = max_length;
        this.map = new HashMap<>();
        map.put(0, "celebrate");
        map.put(1, "clap");
        map.put(2, "hug");
        map.put(3, "punch");
        map.put(4, "sad");
        map.put(5, "none");
        try {
            this.model = Module.load(model_path);
        }
        catch (Exception e){
            Log.e("ModuleHandler", "不存在该文件: ", e);
        }

    }

    public static ModelHandler getInstance(String name, int max_length, String model_path){
        if(modelHandler == null){
            modelHandler = new ModelHandler(name, max_length, model_path);
        }
        return modelHandler;
    }

    public static ModelHandler getInstance(){
        if(modelHandler == null){
            Log.i("ModelHandler", "no instance");
        }
        return modelHandler;
    }

    /**
     * Receive points and nomalize them to Tensor, then get the output and map it to pose   .
     * @param points skeletal data
     * @return a pose name
     */
    public String GetMessage(String points){
        String[] arr = points.split(" ");

        float[] values = new float[arr.length];
        for(int i = 0; i < arr.length; i++){
            values[i] = Float.parseFloat(arr[i]);
        }

        float x = values[4];
        float y = values[5];

        for(int i = 0; i < values.length; i+=2){
            values[i] -= x;
            values[i + 1] -= y;
        }
//        Log.i("OUTPUT", "values: " + Arrays.toString(values));

        int k = 0;
        for (int i = len * 48; i < (len + 1) * 48; i++) {
            buffer[i] = values[k++];
        }

        len += 1;
        if(len >= max_length){
            long[] shape = {1, max_length, 24, 2};

            Tensor input = Tensor.fromBlob(buffer, shape);
            if(model != null){
                Tensor output = model.forward(IValue.from(input)).toTensor();
                float[] scores = output.getDataAsFloatArray();
                int key = argmax(output);
                String res = map.get(key);
                len = 0;
                Log.i("OUTPUT", ": " + res);
                Log.i("OUTPUT", Arrays.toString(scores));

                Arrays.fill(buffer, 0);
                return res;
            }
            len = 0;
            Arrays.fill(buffer, 0);
            return "None";
//            return String.valueOf(scores[0]);
        }
        // 还没存满
        return "NULL";
    }

    /**
     * Copies specified asset to the file in /files app directory and returns this file absolute path.
     *
     * @return absolute file path
     */
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    public int argmax(Tensor t){
        float[] arr = t.getDataAsFloatArray();
        int k = 0;
        float max = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] > max){
                max = arr[i];
                k = i;
            }
        }
        return k;
    }
}
