package mk.ukim.finki.wp.blossomhouse.web.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class FileUploadController {

    private final String UPLOAD_DIR = "C:\\Maja\\FAX VII Semestar\\Web programiranje\\Blossom House\\Blossom House\\src\\main\\resources\\static\\Images\\";
    //private final String UPLOAD_DIR = "C:\\Users\\Ena\\Downloads\\Blossom House\\Blossom House\\src\\main\\resources\\static\\Images\\";

    @GetMapping("/show-upload")
    public String main() {
        return "add-product";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(required = false) String editId,
                         RedirectAttributes attributes,
                         HttpServletRequest request) {

        String returnPage = "add-product";
        if (editId != null && !editId.isEmpty()) {
            returnPage = "edit/" + editId;
        }
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/products/" + returnPage;
        }
        String categoryName = (String) request.getSession().getAttribute("categoryName");

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        request.getSession().setAttribute("imageName", fileName);

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + "\\" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/products/" + returnPage;
    }
}
