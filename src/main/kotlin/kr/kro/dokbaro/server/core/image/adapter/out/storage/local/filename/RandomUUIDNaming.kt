package kr.kro.dokbaro.server.core.image.adapter.out.storage.local.filename

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.image.adapter.out.storage.local.FileNameStrategy
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.UUID

@Component
class RandomUUIDNaming : FileNameStrategy {
	override fun generateName(originName: String): String =
		UUIDUtils.uuidToString(UUID.randomUUID()) + "." +
			StringUtils.getFilenameExtension(originName)
}