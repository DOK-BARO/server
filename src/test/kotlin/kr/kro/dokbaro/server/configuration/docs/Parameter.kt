package kr.kro.dokbaro.server.configuration.docs

import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName

data class Parameter(
	val name: String,
	var description: String,
	var ignore: Boolean = false,
	var optional: Boolean = false,
) {
	infix fun means(description: String): Parameter {
		this.description = description
		return this
	}

	infix fun ignore(ignore: Boolean): Parameter {
		this.ignore = ignore
		return this
	}

	infix fun optional(optional: Boolean): Parameter {
		this.optional = optional
		return this
	}

	fun toDescriptor(): ParameterDescriptor = parameterWithName(name).description(description)
}

infix fun String.means(description: String): Parameter =
	Parameter(
		name = this,
		description = description,
	)