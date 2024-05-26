package vn.hvt.spring.BusTicketReservationSystem.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import groovy.lang.StringWriterIOException;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;

public class QRCodeGenerator {
    // lấy đường dẫn mà ứng dụng đang làm việc
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private static final String PATH = "/images/qrcode/";

    public static String generateQRCode(Booking booking) throws StringWriterIOException, IOException, WriterException {


        // TẠO ĐƯỜNG DẪN /images/
        String qrCodePath  = CURRENT_FOLDER+"\\src\\main\\resources\\static\\images\\qrcode\\";

        // tên file
        String qrCodeName = booking.getPhoneNumber() + booking.getId() + "-QRCODE.png";

//        // kiểm tra đường dẫn đã tồn tại chưa
//        if(!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(qrcodePath))) {
//            // tạo các fodel nếu chưa có
//            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(qrcodePath));
//        }

        String id = ""+booking.getId();

        String encodedId = Base64.getEncoder().encodeToString(id.getBytes());


        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(encodedId, BarcodeFormat.QR_CODE, 400, 400);

        Path path = FileSystems.getDefault().getPath(qrCodePath+qrCodeName);


        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return PATH+qrCodeName;

    }
}
//@RestController
//@RequestMapping("/user")
//public class UserController {
//    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User create(@RequestParam String email,
//                       @RequestParam String password,
//                       @RequestParam MultipartFile image) throws IOException {
//        Path staticPath = Paths.get("static");
//        Path imagePath = Paths.get("images");
//        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
//            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
//        }
//        Path file = CURRENT_FOLDER.resolve(staticPath)
//                .resolve(imagePath).resolve(image.getOriginalFilename());
//        try (OutputStream os = Files.newOutputStream(file)) {
//            os.write(image.getBytes());
//        }
//
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setPhoto(imagePath.resolve(image.getOriginalFilename()).toString());
//        return userRepository.save(user);
//    }
//}