package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.AttachmentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.abstracts.AttachmentService;

import java.util.List;

@RestController
@RequestMapping("/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/upload/{taskId}")
    public ResponseEntity<AttachmentResponse> uploadFile(@PathVariable Long taskId, @RequestParam("file")MultipartFile file){
        AttachmentResponse attachmentResponse= this.attachmentService.uploadFileToTask(taskId,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentResponse);
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte []> downloadFile(@PathVariable Long attachmentId){
        byte [] fileData=this.attachmentService.downloadAttachment(attachmentId);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(fileData,headers,HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<AttachmentResponse>> getAttachmentsByTask(@PathVariable Long taskId){
        return ResponseEntity.ok(this.attachmentService.getAttachmentsByTaskId(taskId));
    }
}
