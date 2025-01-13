package com.lezhin.clone.backend.dto;

import lombok.Getter;
import lombok.Setter;

public class PageDto {
    public static class List {

        @Getter
        @Setter
        public static class Res {
            private Long pageId;
            private String linkName;
            private java.util.List<PagePictureDto> pagePictures;

            @Getter
            @Setter
            public static class PagePictureDto {
                private PictureDto picture;

                @Getter
                @Setter
                public static class PictureDto {
                    private String name;
                    private String url;
                }
            }
        }
    }
    public static class Detail {

        @Getter
        @Setter
        public static class Res {
            private Long pageId;
            private String linkName;
            private java.util.List<PagePictureDto> pagePictures;
            private java.util.List<PageComicDto> pageComics;

            @Getter
            @Setter
            public static class PagePictureDto {
                private PictureDto picture;

                @Getter
                @Setter
                public static class PictureDto {
                    private String name;
                    private String url;
                }
            }

            @Getter
            @Setter
            public static class PageComicDto {
                private ComicDto comic;
                private String desc;
                private String subDesc;

                @Getter
                @Setter
                public static class ComicDto {
                    private String synopsis;
                    private String title;
                    private String linkName;
                    private String thumbnail;
                    private ArtistDto author;
                    private ArtistDto writer;
                    private ArtistDto illustrator;
                    private ArtistDto originalAuthor;
                    private java.util.List<ComicTagDto> comicTags;
            
                    @Getter
                    @Setter
                    public static class ArtistDto {
                        private Long artistId;
                        private String name;
                        private String linkName;
                    }
            
                    @Getter
                    @Setter
                    public static class ComicTagDto {
                        private TagDto tag;
            
                        @Getter
                        @Setter
                        public static class TagDto {
                            private Long tagId;
                            private String name;
                        }
                    }
                }
            }
        }
    }
}