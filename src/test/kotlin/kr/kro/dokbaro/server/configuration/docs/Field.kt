package kr.kro.dokbaro.server.configuration.docs

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes

data class Field(
	val name: String,
	val type: FieldType,
	var description: String? = null,
	var example: String? = null,
	var default: String? = null,
	var ignore: Boolean = false,
	var optional: Boolean = false,
) {
	infix fun means(description: String): Field {
		this.description = description
		return this
	}

	infix fun example(example: String): Field {
		this.example = example
		return this
	}

	infix fun default(default: String): Field {
		this.default = default
		return this
	}

	infix fun ignore(ignore: Boolean): Field {
		this.ignore = ignore
		return this
	}

	infix fun optional(optional: Boolean): Field {
		this.optional = optional
		return this
	}

	fun toDescriptor(): FieldDescriptor {
		val descriptor: FieldDescriptor =
			fieldWithPath(name)
				.type(type)
				.description(description)
				.attributes(*toAttributes().toTypedArray())

		if (ignore) {
			descriptor.ignored()
		}
		if (optional) {
			descriptor.optional()
		}

		return descriptor
	}

	private fun toAttributes(): List<Attributes.Attribute> =
		buildList {
			example?.let { add(Attributes.Attribute("example", it)) }
			default?.let { add(Attributes.Attribute("default", it)) }
		}
}

infix fun String.type(fieldType: FieldType): Field =
	Field(
		name = this,
		type = fieldType,
	)