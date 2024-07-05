package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.JenissuratsResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.KlasifikasiResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.LoginResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Lpbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Npbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SifatResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SpResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SptResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.UnitResponse;

import java.util.List;


import retrofit2.Call;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
//login
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

//klasifikasi
    @GET("klasifikasi")
    Call<KlasifikasiResponse> klasifikasi(
            @Query("kode") String kode,
            @Query("perihal") String perihal
    );
    @POST("klasifikasi")
    Call<KlasifikasiResponse.Klasifikasi> addKlasifikasi(
            @Query("kode") String kode,
            @Query("perihal") String perihal
    );

    @DELETE("klasifikasi/{id}")
    Call<Void> deleteKlasifikasi(@Path("id") int id);

    @FormUrlEncoded
    @PUT("klasifikasi/{id}")
    Call<KlasifikasiResponse.Klasifikasi> updateKlasifikasi(
            @Path("id") int id,
            @Query("kode") String kode,
            @Query("perihal") String perihal

    );


// jenis surat
    @GET("jenissurats")
    Call<JenissuratsResponse> jenissurats(
            @Query("nama") String kode,
            @Query("keterangan") String perihal
    );
    @POST("jenissurats")
    Call<JenissuratsResponse.Jenissurats> addJenis(
            @Query("nama") String kode,
            @Query("keterangan") String perihal
    );


//unit
    @GET("units")
    Call<UnitResponse> getUnit(
            @Query("nama_unit") String nama_unit
    );

    @POST("units")
    @FormUrlEncoded
    Call<UnitResponse.UnitData> addUnit(
            @Field("nama_unit") String nama_unit
    );
    @DELETE("units/{id}")
    Call<Void> deleteUnit(@Path("id") int id);

    @FormUrlEncoded
    @PUT("units/{id}")
    Call<UnitResponse.UnitData> updateUnit(
            @Path("id") int id,
            @Field("nama_unit") String nama_unit
    );

// sifat surat
    @GET("sifatsurats")
    Call<SifatResponse> getSifat(
            @Query("nama") String nama
    );

    @POST("sifatsurats")
    @FormUrlEncoded
    Call<SifatResponse.SifatData> addSifat(
            @Field("nama") String nama
    );

    @PUT("sifatsurats/{id}")
    @FormUrlEncoded
    Call<SifatResponse.SifatData> updateSifat(
            @Path("id") int id,
            @Field("nama") String nama
    );
    @DELETE("sifatsurats/{id}")
    Call<Void> deleteSifat(
            @Path("id") int id
    );



// sp
    @GET("sp")
    Call<SpResponse> getSp(
            @Query("nama") String nama,
            @Query("nomor") String nomor,
            @Query("tanggal") String tanggal,
            @Query("tujuan") String tujuan
    );
    @POST("sp")
    Call<SpResponse.SpData> addSp(
            @Query("nama") String nama,
            @Query("nomor") String nomor,
            @Query("tanggal") String tanggal,
            @Query("tujuan") String tujuan
    );

    @DELETE("sp/{id}")
    Call<Void> deleteSp(@Path("id") int id);

    @FormUrlEncoded
    @PUT("sp/{id}")
    Call<SpResponse.SpData> updateSp(
            @Path("id") int id,
            @Field("nama") String nama,
            @Field("nomor") String nomor,
            @Field("tanggal") String tanggal,
            @Field("tujuan") String tujuan
    );


//    spt
    @GET("spt")
    Call<SptResponse> getSpt(
            @Query("nama") String nama,
            @Query("nomor") String nomor,
            @Query("tanggal") String tanggal,
            @Query("tujuan") String tujuan
    );
    @POST("spt")
    Call<SptResponse.SptData> addSpt(
            @Query("nama") String nama,
            @Query("nomor") String nomor,
            @Query("tanggal") String tanggal,
            @Query("tujuan") String tujuan
    );
    @DELETE("spt/{id}")
    Call<Void> deleteSpt(@Path("id") int id);
    @FormUrlEncoded
    @PUT("spt/{id}")
    Call<SptResponse.SptData> updateSpt(
            @Path("id") int id,
            @Field("nama") String nama,
            @Field("nomor") String nomor,
            @Field("tanggal") String tanggal,
            @Field("tujuan") String tujuan
    );
//    end spt

@POST("lpbph")
Call<Lpbph> createLpbph(
        @Query("nama_barang") String namaBarang,
        @Query("merk") String merk,
        @Query("vol") int vol,
        @Query("satuan") String satuan,
        @Query("harga_satuan") int hargaSatuan
);

    @GET("lpbph")
    Call<List<Lpbph>> getLpbph(
            @Query("nama_barang") String namaBarang,
            @Query("merk") String merk,
            @Query("vol") int vol,
            @Query("satuan") String satuan,
            @Query("harga_satuan") int hargaSatuan
    );


    @DELETE("lpbph/{id}")
    Call<Void> deletelpbph(@Path("id") int id);


//    @GET("lpbph/print/{id}")
//    Call<byte[]> getLpbphPdf(@Path("id") int lpbphId);

    @GET("nota")
    Call<List<Npbph>> getNpbph(
            @Query("nama_barang") String namaBarang,
            @Query("merk") String merk,
            @Query("vol") int vol,
            @Query("satuan") String satuan,
            @Query("harga_satuan") int hargaSatuan
    );


    @DELETE("nota/{id}")
    Call<Void> deletenpbph(@Path("id") int id);



}
