package kr.kro.dokbaro.server.common.util

import java.nio.ByteBuffer
import java.util.UUID

object UUIDUtils {
	fun uuidToByteArray(uuid: UUID): ByteArray {
		val byteBuffer = ByteBuffer.allocate(16)
		byteBuffer.putLong(uuid.mostSignificantBits)
		byteBuffer.putLong(uuid.leastSignificantBits)
		return byteBuffer.array()
	}

	fun byteArrayToUUID(byteArray: ByteArray): UUID {
		val byteBuffer = ByteBuffer.wrap(byteArray)
		val mostSigBits = byteBuffer.long
		val leastSigBits = byteBuffer.long
		return UUID(mostSigBits, leastSigBits)
	}

	fun uuidToString(uuid: UUID): String = uuid.toString()

	fun stringToUUID(string: String): UUID = UUID.fromString(string)
}