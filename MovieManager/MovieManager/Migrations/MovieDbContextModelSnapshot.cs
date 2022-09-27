﻿// <auto-generated />
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using MovieManager.database;

#nullable disable

namespace MovieManager.Migrations
{
    [DbContext(typeof(MovieDbContext))]
    partial class MovieDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "6.0.9")
                .HasAnnotation("Relational:MaxIdentifierLength", 64);

            modelBuilder.Entity("MovieManager.database.models.DailyBoxOffice", b =>
                {
                    b.Property<int>("rankNo")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasColumnName("rank_no");

                    b.Property<int>("audiCnt")
                        .HasColumnType("int")
                        .HasColumnName("audi_cnt");

                    b.Property<string>("movieCode")
                        .IsRequired()
                        .HasColumnType("varchar(255)")
                        .HasColumnName("movie_code");

                    b.Property<string>("newOrOld")
                        .IsRequired()
                        .HasColumnType("longtext")
                        .HasColumnName("new_or_old");

                    b.Property<string>("openDate")
                        .IsRequired()
                        .HasColumnType("longtext")
                        .HasColumnName("open_date");

                    b.Property<int>("rankInten")
                        .HasColumnType("int")
                        .HasColumnName("rank_inten");

                    b.HasKey("rankNo");

                    b.HasIndex("movieCode");

                    b.ToTable("daily_box_office");
                });

            modelBuilder.Entity("MovieManager.database.models.Movie", b =>
                {
                    b.Property<string>("movieCode")
                        .HasColumnType("varchar(255)")
                        .HasColumnName("movie_code");

                    b.Property<string>("actors")
                        .HasColumnType("longtext")
                        .HasColumnName("actors");

                    b.Property<string>("companyName")
                        .HasColumnType("longtext")
                        .HasColumnName("company");

                    b.Property<string>("director")
                        .HasColumnType("longtext")
                        .HasColumnName("director");

                    b.Property<string>("genre")
                        .HasColumnType("longtext")
                        .HasColumnName("genre");

                    b.Property<string>("imageUrl")
                        .HasColumnType("longtext")
                        .HasColumnName("image_url");

                    b.Property<string>("nation")
                        .HasColumnType("longtext")
                        .HasColumnName("nation");

                    b.Property<string>("productStatus")
                        .HasColumnType("longtext")
                        .HasColumnName("product_status");

                    b.Property<string>("productYear")
                        .HasColumnType("longtext")
                        .HasColumnName("product_year");

                    b.Property<int>("showTime")
                        .HasColumnType("int")
                        .HasColumnName("show_time");

                    b.Property<string>("titleEn")
                        .HasColumnType("longtext")
                        .HasColumnName("title_en");

                    b.Property<string>("titleKor")
                        .HasColumnType("longtext")
                        .HasColumnName("title_kor");

                    b.Property<string>("watchGrade")
                        .HasColumnType("longtext")
                        .HasColumnName("watch_grade");

                    b.HasKey("movieCode");

                    b.ToTable("movie");
                });

            modelBuilder.Entity("MovieManager.database.models.WeeklyBoxOffice", b =>
                {
                    b.Property<int>("rankNo")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasColumnName("rank_no");

                    b.Property<int>("audiCnt")
                        .HasColumnType("int")
                        .HasColumnName("audi_cnt");

                    b.Property<string>("movieCode")
                        .IsRequired()
                        .HasColumnType("varchar(255)")
                        .HasColumnName("movie_code");

                    b.Property<string>("newOrOld")
                        .IsRequired()
                        .HasColumnType("longtext")
                        .HasColumnName("new_or_old");

                    b.Property<string>("openDate")
                        .IsRequired()
                        .HasColumnType("longtext")
                        .HasColumnName("open_date");

                    b.Property<int>("rankInten")
                        .HasColumnType("int")
                        .HasColumnName("rank_inten");

                    b.HasKey("rankNo");

                    b.HasIndex("movieCode");

                    b.ToTable("weekly_box_office");
                });

            modelBuilder.Entity("MovieManager.database.models.DailyBoxOffice", b =>
                {
                    b.HasOne("MovieManager.database.models.Movie", "movie")
                        .WithMany()
                        .HasForeignKey("movieCode")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("movie");
                });

            modelBuilder.Entity("MovieManager.database.models.WeeklyBoxOffice", b =>
                {
                    b.HasOne("MovieManager.database.models.Movie", "movie")
                        .WithMany()
                        .HasForeignKey("movieCode")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("movie");
                });
#pragma warning restore 612, 618
        }
    }
}
