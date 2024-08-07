    package com.team6.backend.theater.region.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Entity
    public class Gugunnm {
        @Id
        @Column(name = "gugunnm")
        private String gugunnm;

        @Column(name = "sidonm")
        private String sidonm;


    }
