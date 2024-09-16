package kr.kro.dokbaro.server.core.image.application.port.out

import org.springframework.web.multipart.MultipartFile

fun interface InsertImagePort {
	fun insert(
		file: MultipartFile,
		middlePath: String,
	): String
}