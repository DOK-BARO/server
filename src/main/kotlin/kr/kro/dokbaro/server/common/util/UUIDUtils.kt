package kr.kro.dokbaro.server.common.util

import java.nio.ByteBuffer
import java.util.UUID

/**
 * UUID에 관련한 util 클래스입니다.
 */
object UUIDUtils {
	/**
	 * UUID -> ByteArray
	 */
	fun uuidToByteArray(uuid: UUID): ByteArray {
		val byteBuffer = ByteBuffer.allocate(16)
		byteBuffer.putLong(uuid.mostSignificantBits)
		byteBuffer.putLong(uuid.leastSignificantBits)
		return byteBuffer.array()
	}

	/**
	 *  ByteArray -> UUID
	 */
	fun byteArrayToUUID(byteArray: ByteArray): UUID {
		val byteBuffer = ByteBuffer.wrap(byteArray)
		val mostSigBits = byteBuffer.long
		val leastSigBits = byteBuffer.long
		return UUID(mostSigBits, leastSigBits)
	}

	/**
	 * UUID -> String
	 */
	fun uuidToString(uuid: UUID): String = uuid.toString()

	/**
	 * String -> UUID
	 */
	fun stringToUUID(str: String): UUID = UUID.fromString(str)
}