package me.doyoung.jpaconcurrency.treatment.dto;

import me.doyoung.jpaconcurrency.treatment.domain.Treatment;

public class TreatmentDtos {
    public static class Response {
        private Long id;

        public Response() {
        }

        public Response(Treatment treatment) {
            this.id = treatment.getId();
        }


        public Long getId() {
            return id;
        }
    }

    public static class Request {
        private String name;

        public Request() {
        }

        public Request(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
