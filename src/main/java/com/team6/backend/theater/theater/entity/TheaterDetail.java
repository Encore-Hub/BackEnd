    package com.team6.backend.theater.theater.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    @Entity
    @Table(name = "theater")
    public class TheaterDetail {

        @Id
        @Column(name = "mt10id")
        private String mt10id;

        @Column(name = "fcltychartr")
        private String fcltychartr;

        @Column(name = "sidonm")
        private String sidonm;

        @Column(name = "gugunnm")
        private String gugunnm;

        @Column(name = "fcltynm")
        private String fcltynm;

        @Column(name = "seatscale")
        private int seatscale;

        @Column(name = "mt13cnt")
        private int mt13cnt;

        @Column(name = "telno")
        private String telno;

        @Column(name = "relateurl")
        private String relateurl;

        @Column(name = "adres")
        private String adres;

        @Column(name = "la")
        private double la;

        @Column(name = "lo")
        private double lo;

        @Column(name = "parkinglot")
        private String parkinglot;

    }
