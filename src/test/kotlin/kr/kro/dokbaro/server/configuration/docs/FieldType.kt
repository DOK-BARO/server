package kr.kro.dokbaro.server.configuration.docs

import org.springframework.restdocs.payload.JsonFieldType

sealed class FieldType(
	val type: JsonFieldType,
	val format: String? = null,
	val additionalDescription: String? = null,
) {
	data object NUMBER : FieldType(JsonFieldType.NUMBER)

	data object STRING : FieldType(JsonFieldType.STRING)

	data object BOOLEAN : FieldType(JsonFieldType.BOOLEAN)

	data object OBJECT : FieldType(JsonFieldType.OBJECT)

	data object DATE : FieldType(
		type = JsonFieldType.STRING,
		format = "yyyy-MM-dd",
	)

	data object DATETIME : FieldType(
		type = JsonFieldType.STRING,
		format = "yyyy-MM-dd HH:mm:ss",
	)

	data class ARRAY(
		private val elementType: FieldType,
	) : FieldType(
			type = JsonFieldType.ARRAY,
			additionalDescription = "element type : $elementType",
		)

	class ENUM<T : Enum<T>>(
		enumClass: Class<T>,
	) : FieldType(
			type = JsonFieldType.STRING,
			additionalDescription = "enum example: $${enumClass.enumConstants.joinToString(", ") { it.name }}]",
		)
}