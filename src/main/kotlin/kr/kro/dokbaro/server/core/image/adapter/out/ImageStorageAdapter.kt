package kr.kro.dokbaro.server.core.image.adapter.out

import kr.kro.dokbaro.server.core.image.adapter.out.storage.local.LocalImageStorage
import kr.kro.dokbaro.server.core.image.application.port.out.InsertImagePort
import kr.kro.dokbaro.server.core.image.application.port.out.LoadImagePort
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class ImageStorageAdapter(
	private val localImageStorage: LocalImageStorage,
) : InsertImagePort,
	LoadImagePort {
	override fun insert(
		file: MultipartFile,
		middlePath: String,
	): String = localImageStorage.save(file, middlePath)

	override fun load(locationPath: String): File = localImageStorage.load(locationPath)
}