package kr.kro.dokbaro.server.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 시간에 관련한 util 클래스입니다.
 */
object TimeUtils {
	fun zone(): ZoneId = ZoneId.of("Asia/Seoul")

	fun timeToInstant(time: LocalDateTime): Instant = time.atZone(zone()).toInstant()
}