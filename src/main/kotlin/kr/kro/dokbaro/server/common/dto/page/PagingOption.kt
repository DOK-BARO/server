package kr.kro.dokbaro.server.common.dto.page

data class PagingOption(
	val offset: Long,
	val limit: Long,
) {
	companion object {
		fun of(
			page: Long,
			size: Long,
		) = PagingOption((page - 1) * size, size)
	}
}