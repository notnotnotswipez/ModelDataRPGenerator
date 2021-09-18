package me.swipez.ModelDataRPGenerator;

import me.swipez.ModelDataRPGenerator.group.QueueGroup;
import me.swipez.ModelDataRPGenerator.savedata.CustomModelDataItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputReader {

    static File outputPack;
    static File models;
    static File textures;
    static File customModelFolder;

    static int packNumber = 7; // Number changes with every big version, 1.17 uses ID 7.

    public static List<CustomModelDataItem> customModelDataItems = new ArrayList<>();

    public static void readInput() throws IOException {
        for (File file : Main.inputFolder.listFiles()){
            if (file.isFile()){
                if (file.getName().contains(".json")){
                    Main.LOGGER.log(Level.INFO, "Found json model: "+file.getName());
                    String[] splitContext = file.getName().split("_");

                    File textureFile = new File(Main.inputFolder.getPath()+"/"+file.getName().replace(".json", "")+".png");
                    if (file.exists()){
                        Main.LOGGER.log(Level.INFO, "Found associated texture file: "+textureFile.getName());
                        int modelNumber = Integer.valueOf(splitContext[splitContext.length-1].replace(".json", ""));
                        String name = file.getName().replace("_"+modelNumber+".json", "");
                        Main.LOGGER.log(Level.INFO, name);
                        customModelDataItems.add(new CustomModelDataItem(name, modelNumber, file, textureFile));
                        Main.LOGGER.log(Level.INFO, "Added to queue.");
                    }
                    else {
                        throw new IOException();
                    }
                }
            }
        }
        if (!customModelDataItems.isEmpty()){
            makeDefaultPackDirectory();
            makeModelAndTextureDirectories();
            segmentItemsAndCreate();
            for (CustomModelDataItem customModelDataItem : customModelDataItems){
                File modelDestinationFolder = new File(textures.getPath()+"/"+customModelDataItem.getReplacementItem());
                if (!modelDestinationFolder.exists()){
                    modelDestinationFolder.mkdir();
                }
                customModelDataItem.moveFilesToLocations(new File(customModelFolder.getPath()+"/"+customModelDataItem.getModelDataNumber()+".json"), new File(modelDestinationFolder.getPath()+"/"+customModelDataItem.getModelDataNumber()+".png"));
            }
            Main.LOGGER.log(Level.INFO, "Made Resource pack. Check the output folder of this program for: "+outputPack.getName());
        }
        else {
            Main.LOGGER.log(Level.WARNING, "No textures or models we're provided, so the RP was not made.");
        }

    }

    private static void makeDefaultPackDirectory() throws IOException {
        int highestNumberFound = 0;
        for (File file : Main.outputFolder.listFiles()){
            if (file.isDirectory()){
                String[] split = file.getName().split("_");
                if (highestNumberFound < Integer.parseInt(split[1])){
                    highestNumberFound = Integer.parseInt(split[1]);
                }
            }
        }
         outputPack = new File(Main.outputFolder.getPath()+"/generatedPack_"+(highestNumberFound+1));

        if (!outputPack.exists()){
            outputPack.mkdir();
        }

        JSONObject packMeta = new JSONObject();

        JSONObject packInternals = new JSONObject();
        packInternals.put("pack_format", packNumber);
        packInternals.put("description", "Made via ModelDataRPGenerator By notnotnotswipez");

        packMeta.put("pack", packInternals);

        createFileWithText(new File(outputPack.getPath()+"/pack.mcmeta"), packMeta.toString());
        Main.LOGGER.log(Level.INFO, "Made pack.mcmeta");
    }

    private static void createFileWithText(File path, String text) throws IOException {
        FileWriter file = new FileWriter(path.getPath());
        file.write(text);
        file.close();
    }

    private static void segmentItemsAndCreate(){
        Main.LOGGER.log(Level.INFO, "Beginning the process for generation...");
        HashMap<String, QueueGroup> segmentedGroups = new HashMap<>();

        for (CustomModelDataItem customModelDataItem : customModelDataItems){
            if (!segmentedGroups.containsKey(customModelDataItem.getReplacementItem())){
                segmentedGroups.put(customModelDataItem.getReplacementItem(), new QueueGroup());

                QueueGroup queueGroup = segmentedGroups.get(customModelDataItem.getReplacementItem());
                queueGroup.customModelDataItems.add(customModelDataItem);

                segmentedGroups.put(customModelDataItem.getReplacementItem(), queueGroup);

                Main.LOGGER.log(Level.INFO, "Found a new requested item for: "+customModelDataItem.getReplacementItem());
            }
            else {
                QueueGroup queueGroup = segmentedGroups.get(customModelDataItem.getReplacementItem());
                queueGroup.customModelDataItems.add(customModelDataItem);

                segmentedGroups.put(customModelDataItem.getReplacementItem(), queueGroup);

                Main.LOGGER.log(Level.INFO, "Found another requested item which affects an already registered item. (This is fine itll work lol)");
            }
        }

        for (String string : segmentedGroups.keySet()){
            Main.LOGGER.log(Level.INFO, "Making files for: "+string);
            makeFilesForCustomItem(segmentedGroups.get(string).customModelDataItems);
        }

    }

    private static void makeModelAndTextureDirectories(){
        models = new File(outputPack.getPath()+"/assets/minecraft/models/item");
        customModelFolder = new File(outputPack.getPath()+"/assets/minecraft/models/item/custommodelfolder");
        textures = new File(outputPack.getPath()+"/assets/minecraft/textures/item");

        if (!models.exists()){
            Main.LOGGER.log(Level.INFO, "Made item models directory.");
            models.mkdirs();
        }
        if (!textures.exists()){
            Main.LOGGER.log(Level.INFO, "Made item textures directory.");
            textures.mkdirs();
        }
        if (!customModelFolder.exists()){
            customModelFolder.mkdirs();
        }
    }

    private static void makeFilesForCustomItem(List<CustomModelDataItem> customModelDataItems){
        String mainItem = null;
        for (CustomModelDataItem customModelDataItem : customModelDataItems){
            mainItem = customModelDataItem.getReplacementItem();
        }

        Main.LOGGER.log(Level.INFO, "Constructing model file for: "+mainItem);

        JSONObject modelFile = new JSONObject();
        modelFile.put("parent", "item/handheld");

        JSONObject textures = new JSONObject();
        textures.put("layer0", "item/"+mainItem);

        modelFile.put("textures", textures);

        JSONArray overrideArray = new JSONArray();

        for (CustomModelDataItem customModelDataItem : customModelDataItems){
            JSONObject predicate = new JSONObject();
            JSONObject modelData = new JSONObject();
            modelData.put("custom_model_data", customModelDataItem.getModelDataNumber());

            predicate.put("predicate", modelData);
            predicate.put("model", "item/custommodelfolder/"+(customModelDataItem.getModelDataNumber()));

            Main.LOGGER.log(Level.INFO, "Added predicate for model ID: "+customModelDataItem.getModelDataNumber());

            overrideArray.put(predicate);
        }

        modelFile.put("overrides", overrideArray);

        try {
            createFileWithText(new File(models+"/"+mainItem+".json"), modelFile.toString());
            Main.LOGGER.log(Level.INFO, "Created model file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
