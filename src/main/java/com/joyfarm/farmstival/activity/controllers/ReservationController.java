    package com.joyfarm.farmstival.activity.controllers;

    import com.joyfarm.farmstival.activity.entities.Reservation;
    import com.joyfarm.farmstival.activity.services.ReservationApplyService;
    import com.joyfarm.farmstival.activity.services.ReservationInfoService;
    import com.joyfarm.farmstival.activity.validators.ReservationValidator;
    import com.joyfarm.farmstival.global.ListData;
    import com.joyfarm.farmstival.global.Utils;
    import com.joyfarm.farmstival.global.exceptions.BadRequestException;
    import com.joyfarm.farmstival.global.rests.JSONData;
    import com.joyfarm.farmstival.member.MemberUtil;
    import com.joyfarm.farmstival.member.entities.Member;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.validation.Errors;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/activity/reservation")
    @RequiredArgsConstructor
    public class ReservationController {
        private final ReservationApplyService applyService;
        private final ReservationInfoService infoService;
        private final ReservationValidator validator;
        private final MemberUtil memberUtil;
        private final Utils utils;

        /**
         * 예약 접수
         *
         * @return
         */
        @PostMapping
        public ResponseEntity<JSONData> apply(@Valid RequestReservation form, Errors errors) {
            validator.validate(form, errors);

            if (errors.hasErrors()) {
                throw new BadRequestException(utils.getErrorMessages(errors));
            }

            Reservation reservation = applyService.apply(form);

            HttpStatus status = HttpStatus.CREATED;
            JSONData jsonData = new JSONData(reservation);
            jsonData.setStatus(status);

            return ResponseEntity.status(status).body(jsonData);
        }

        @GetMapping
        public JSONData list(@ModelAttribute ReservationSearch search) {
            Member member = memberUtil.getMember();
            search.setMemberSeqs(List.of(member.getSeq()));

            ListData<Reservation> data = infoService.getList(search);

            return new JSONData(data);
        }

        @GetMapping("/info/{seq}")
        public JSONData info(@PathVariable("seq") Long seq) {
            Reservation reservation = infoService.get(seq, true);

            return new JSONData(reservation);
        }

        @GetMapping("/admin/list")
        @PreAuthorize("hasAnyAuthority('ADMIN')")
        public JSONData adminlist(@ModelAttribute ReservationSearch search) {
            ListData<Reservation> data = infoService.getList(search);

            return new JSONData(data);
        }

        @GetMapping("/admin/info/{seq}")
        @PreAuthorize("hasAnyAuthority('ADMIN')")
        public JSONData adminInfo(@PathVariable("seq") Long seq) {
            Reservation reservation = infoService.get(seq);

            return new JSONData(reservation);
        }
    }
