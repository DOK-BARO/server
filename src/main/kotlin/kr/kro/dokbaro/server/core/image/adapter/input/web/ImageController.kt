package kr.kro.dokbaro.server.core.image.adapter.input.web

import kr.kro.dokbaro.server.core.image.application.port.input.FindImageUseCase
import kr.kro.dokbaro.server.core.image.application.port.input.UploadImageUseCase
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files

@RestController
@RequestMapping("/images")
class ImageController(
	private val uploadImageUseCase: UploadImageUseCase,
	private val findImageUseCase: FindImageUseCase,
) {
	@PostMapping("/{target}")
	fun upload(
		@PathVariable target: String,
		@RequestPart file: MultipartFile,
	) = UploadImageResponse(uploadImageUseCase.upload(file, target))

	@GetMapping
	fun get(
		@RequestParam path: String,
	): ResponseEntity<Resource> {
		val file: File = findImageUseCase.findBy(path)

		val mimeType = Files.probeContentType(file.toPath()) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE

		val resource = InputStreamResource(FileInputStream(file))

		return ResponseEntity
			.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${file.name}\"")
			.contentLength(file.length())
			.contentType(MediaType.parseMediaType(mimeType))
			.body(resource)
	}
}