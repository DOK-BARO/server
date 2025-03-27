package kr.kro.dokbaro.server.configuration.docs.builder

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class StringPairDslBuilder(
	val map: MultiValueMap<String, String> = LinkedMultiValueMap(),
) {
	infix fun String.to(description: String) {
		map[this] = description
	}

	fun exists(): Boolean = map.isNotEmpty()
}