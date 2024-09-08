package kr.kro.dokbaro.server.core.image.adapter.out.storage.local

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class LocalImageStorage(
	@Value("\${local.image.storage.host-url}") val hostUrl: String,
	@Value("\${local.image.storage.location-path}") val locationPath: String,
	private val fileNameStrategy: FileNameStrategy,
) {
	fun save(
		file: MultipartFile,
		middlePath: String,
	): String {
		val fileName =
			fileNameStrategy.generateName(
				file.originalFilename
					?: throw InvalidFileNameException(file.originalFilename),
			)

		val totalPath = combinePath(locationPath, middlePath)

		file.transferTo(File(totalPath + middlePath, fileName))

		return hostUrl + totalPath + fileName
	}

	private fun combinePath(
		rootPath: String,
		middlePath: String,
	): String = File(rootPath).resolve(middlePath).path + "/"

	fun load(locationPath: String): File = File(locationPath)
}