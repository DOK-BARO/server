package kr.kro.dokbaro.server.configuration.docs.builder

import kr.kro.dokbaro.server.configuration.docs.Field
import kr.kro.dokbaro.server.configuration.docs.FieldType
import org.springframework.restdocs.payload.FieldDescriptor

data class FieldDslBuilder(
	val fields: MutableList<Field> = mutableListOf(),
) {
	infix fun String.type(type: FieldType): Field {
		val field = Field(this, type)

		fields.add(field)

		return field
	}

	fun toDescriptor(): List<FieldDescriptor> = fields.map { it.toDescriptor() }

	fun exists(): Boolean = fields.isNotEmpty()
}