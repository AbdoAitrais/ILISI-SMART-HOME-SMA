package ma.ilisi.smarthome.models;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.opencv.imgproc.Imgproc.rectangle;

class DetectFaceDemo {
    public static void run(MultipartFile file) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load("src/main/resources/classifiers/haarcascade_frontalface_alt.xml");

        // Convert MultipartFile to a temporary file
        Path tempFile = Files.createTempFile("temp_image", ".jpg");
        file.transferTo(tempFile);

        // Read the image using OpenCV
        Mat image = Imgcodecs.imread(tempFile.toString());

        // Detecting faces
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        // Creating a rectangular box showing faces detected
        for (Rect rect : faceDetections.toArray()) {
            rectangle(image, new Point(rect.x, rect.y), new Point(rect.width + rect.x, rect.height + rect.y),
                    new Scalar(0, 255, 0));
        }

        // Saving the output image
        String filename = "Output.jpg";
        System.out.println("Face Detected Successfully");
        Imgcodecs.imwrite("src/main/resources/images/" + filename, image);

        // Delete the temporary file
        Files.deleteIfExists(tempFile);
    }

    public static void main(String[] args) throws IOException {
        // Replace this with your actual MultipartFile object
        MultipartFile inputFile = MultipartFileCreator.createMultipartFile();

        try {
            DetectFaceDemo.run(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
