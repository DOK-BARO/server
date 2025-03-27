package kr.kro.dokbaro.server.configuration.docs.builder

import kr.kro.dokbaro.server.configuration.docs.Parameter
import org.springframework.restdocs.request.ParameterDescriptor

data class ParameterDslBuilder(
	val parameters: MutableList<Parameter> = mutableListOf(),
) {
	infix fun String.means(description: String): Parameter {
		val field = Parameter(this, description)

		parameters.add(field)

		return field
	}

	fun toDescriptor(): List<ParameterDescriptor> = parameters.map { it.toDescriptor() }

	fun exists(): Boolean = parameters.isNotEmpty()
}