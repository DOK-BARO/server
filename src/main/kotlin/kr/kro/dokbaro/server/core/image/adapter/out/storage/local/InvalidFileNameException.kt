package kr.kro.dokbaro.server.core.image.adapter.out.storage.local

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class InvalidFileNameException(
	fileName: String?,
) : BadRequestException("invalid file name : $fileName")