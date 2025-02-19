package kr.kro.dokbaro.server.core.book.batch.registration

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BookRegistrationScheduler {
	@Scheduled(cron = "0 0 3 * * *")
	fun schedule() {
	}
}