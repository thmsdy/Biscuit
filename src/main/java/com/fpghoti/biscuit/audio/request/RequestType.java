package com.fpghoti.biscuit.audio.request;

public enum RequestType {
	
	YOUTUBE{
        @Override
        public String toString() {
            return "YouTube";
        }
    },
	YOUTUBE_IMMEDIATE{
        @Override
        public String toString() {
            return "YouTube - Immediate";
        }
    },
	YOUTUBE_PRIORITY{
        @Override
        public String toString() {
            return "YouTube - Priority";
        }
    },
	SOUNDCLOUD{
        @Override
        public String toString() {
            return "Soundcloud";
        }
    },
	SOUNDCLOUD_PRIORITY{
        @Override
        public String toString() {
            return "Soundcloud - Priority";
        }
    },
	FILE{
        @Override
        public String toString() {
            return "File";
        }
    },
	FILE_PRIORITY{
        @Override
        public String toString() {
            return "File - Priority";
        }
    };

}
