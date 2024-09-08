package kr.kro.dokbaro.server.core.image.application.port.input

import org.springframework.web.multipart.MultipartFile

fun interface UploadImageUseCase {
	fun upload(
		file: MultipartFile,
		target: String,
	): String
}