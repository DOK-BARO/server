package kr.kro.dokbaro.server.abstract

data class Path(
	val endPoint: String,
	val pathVariable: Collection<String> = listOf(),
) {
	constructor(endPoint: String, vararg pathVariable: String) : this(endPoint, pathVariable.toList())
}