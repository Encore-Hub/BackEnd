package com.team6.backend.theater.scheduler;


import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import com.team6.backend.theater.api.service.TheaterPfmcDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TheaterScheduler {

    @Autowired
    private TheaterListService theaterListService;

    @Autowired
    private TheaterDetailService theaterDetailService;

    @Autowired
    private TheaterPfmcDetailService theaterPfmcDetailService;

    @Scheduled(cron = "0 0 0 * * sun") // 매주 월요일 자정에 실행
    public void scheduleTheaterUpdates() throws Exception {
        theaterListService.fetchAndSaveTheaterList();
        theaterDetailService.fetchAndSaveTheaterDetails();
        theaterPfmcDetailService.fetchAndSaveTheaterPfmcDetails();
    }
}
