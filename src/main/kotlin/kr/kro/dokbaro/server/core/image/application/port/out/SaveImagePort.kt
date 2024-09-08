package kr.kro.dokbaro.server.core.image.application.port.out

import org.springframework.web.multipart.MultipartFile

fun interface SaveImagePort {
	fun save(
		file: MultipartFile,
		middlePath: String,
	): String
}