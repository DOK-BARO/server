package kr.kro.dokbaro.server.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object TimeUtils {
	fun zone(): ZoneId = ZoneId.of("Asia/Seoul")

	fun timeToInstant(time: LocalDateTime): Instant = time.atZone(zone()).toInstant()
}