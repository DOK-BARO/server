package kr.kro.dokbaro.server.common.dto.option

data class PageOption(
	val offset: Long,
	val limit: Long,
) {
	companion object {
		fun of(
			page: Long,
			size: Long,
		) = PageOption(
			offset = (page - 1) * size,
			limit = size,
		)
	}
}