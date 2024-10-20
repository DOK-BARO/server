package kr.kro.dokbaro.server.common.dto.option

data class PageOption(
	val offset: Long,
	val limit: Long,
) {
	companion object {
		fun of(
			page: Long,
			size: Long,
		) = PageOption((page - 1) * size, size)
	}
}