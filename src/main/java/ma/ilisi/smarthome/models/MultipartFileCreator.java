package ma.ilisi.smarthome.models;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MultipartFileCreator {

    public static MultipartFile createMultipartFile() throws IOException {
        // Specify the path to your file
        String filePath = "src/main/resources/images/face.jpg";

        // Create a File object from the path
        File file = new File(filePath);

        // Create a FileInputStream from the File
        FileInputStream fileInputStream = new FileInputStream(file);

        // Create a MockMultipartFile
        return new MockMultipartFile("file", file.getName(), "image/jpeg", fileInputStream);
    }
}
