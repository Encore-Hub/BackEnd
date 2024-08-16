package com.team6.backend.scheduler;


import com.team6.backend.pfmc.api.PfmcApi;
import com.team6.backend.pfmc.api.PfmcListApi;
import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import com.team6.backend.theater.api.service.TheaterPfmcDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private TheaterListService theaterListService;

    @Autowired
    private TheaterDetailService theaterDetailService;

    @Autowired
    private TheaterPfmcDetailService theaterPfmcDetailService;

    @Autowired
    private PfmcListApi pfmclistApi;

    @Autowired
    private PfmcApi pfmcapi ;

    @Scheduled(cron = "0 0 15 * * *") // 매일 낮 12시 에 실행
    public void scheduleTheaterUpdates() throws Exception {

        pfmclistApi.callPfmcListApiJson();
        pfmcapi.callPfmcApiJson();
        theaterListService.fetchAndSaveTheaterList();
        theaterDetailService.fetchAndSaveTheaterDetails();
        theaterPfmcDetailService.fetchAndSaveTheaterPfmcDetails();

    }
}
