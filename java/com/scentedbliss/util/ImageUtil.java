package com.scentedbliss.util;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.Part;

/**
 * @author 23049172 Sabin Devkota
 */

/**
 * Utility class for handling image file uploads.
 * <p>
 * This class provides methods for extracting the file name from a {@link Part}
 * object and uploading the image file to a specified directory on the server.
 * </p>
 */
public class ImageUtil {

    /**
     * Extracts the file name from the given {@link Part} object based on the
     * "content-disposition" header.
     * 
     * <p>
     * This method parses the "content-disposition" header to retrieve the file name
     * of the uploaded image. If the file name cannot be determined, a default name
     * "download.png" is returned.
     * </p>
     * 
     * @param part the {@link Part} object representing the uploaded file.
     * @return the extracted file name. If no filename is found, returns a default
     *         name "download.png".
     */
    public String getImageNameFromPart(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String imageName = null;
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                imageName = imageName.replace("\\", "/"); // Normalize path separators
                int lastSlash = imageName.lastIndexOf("/");
                if (lastSlash >= 0) {
                    imageName = imageName.substring(lastSlash + 1);
                }
            }
        }
        if (imageName == null || imageName.isEmpty()) {
            imageName = "download.png";
        }
        return imageName;
    }

    /**
     * Uploads the image file from the given {@link Part} object to a specified
     * directory on the server.
     * 
     * <p>
     * This method ensures that the directory where the file will be saved exists
     * and creates it if necessary. It writes the uploaded file to the server's file
     * system. Returns {@code true} if the upload is successful, and {@code false}
     * otherwise.
     * </p>
     * 
     * @param part the {@link Part} object representing the uploaded image file.
     * @param rootPath the root path of the servlet context.
     * @param saveFolder the folder where the image should be saved (relative to resources/images).
     * @return {@code true} if the file was successfully uploaded, {@code false}
     *         otherwise.
     */
    public boolean uploadImage(Part part, String rootPath, String saveFolder) {
        String savePath = rootPath + "/resources/images" + saveFolder;
        File fileSaveDir = new File(savePath);

        // Ensure the directory exists
        if (!fileSaveDir.exists()) {
            if (!fileSaveDir.mkdirs()) {
                System.out.println("ImageUtil: Failed to create directory: " + savePath);
                return false;
            }
        }
        try {
            String imageName = getImageNameFromPart(part);
            String filePath = savePath + File.separator + imageName;
            System.out.println("ImageUtil: Saving image to: " + filePath);
            part.write(filePath);
            return true;
        } catch (IOException e) {
            System.out.println("ImageUtil: IOException during image upload: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the save path for the image (relative to the web context).
     */
    public String getSavePath(String saveFolder) {
		return "/Users/soniyasapkota/eclipse-workspace/scented-bliss/src/main/webapp/resources/images/"+saveFolder+"/";
	}
}