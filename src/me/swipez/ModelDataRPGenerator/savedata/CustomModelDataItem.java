package me.swipez.ModelDataRPGenerator.savedata;

import me.swipez.ModelDataRPGenerator.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class CustomModelDataItem {

    private final String replacementItem;
    private final int modelDataNumber;
    private final File modelLocation;
    private final File textureLocation;

    public CustomModelDataItem(String replacementItem, int modelDataNumber, File modelLocation, File textureLocation) {
        this.replacementItem = replacementItem;
        this.modelDataNumber = modelDataNumber;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
    }

    public String getReplacementItem() {
        return replacementItem;
    }

    public int getModelDataNumber() {
        return modelDataNumber;
    }

    public File getModelLocation() {
        return modelLocation;
    }

    public File getTextureLocation() {
        return textureLocation;
    }

    public void moveFilesToLocations(File modelDestination, File textureDestination) throws IOException {
        Files.copy(getModelLocation().toPath(), modelDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(getTextureLocation().toPath(), textureDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
