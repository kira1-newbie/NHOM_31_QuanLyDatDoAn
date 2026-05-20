CREATE DATABASE dbQuanLyDatDoAn;
GO
USE dbQuanLyDatDoAn;
GO

-- ==========================================
-- PHAN 1: TAO BANG & RANG BUOC
-- ==========================================

CREATE TABLE Role (
    RoleId INT IDENTITY(1,1) PRIMARY KEY,
    TenRole VARCHAR(20) UNIQUE NOT NULL -- ADMIN, CUSTOMER, SHIPPER, RESTAURANT
);

CREATE TABLE NguoiDung (
    NguoiDungId CHAR(7) PRIMARY KEY,
    RoleId INT NOT NULL,
    HoTen NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    MatKhau NVARCHAR(255) NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT N'HOẠT ĐỘNG',
    FOREIGN KEY (RoleId) REFERENCES Role(RoleId)
);

CREATE TABLE QuanAn (
    QuanAnId CHAR(7) PRIMARY KEY,
    ChuQuanId CHAR(7) UNIQUE NOT NULL,
    TenQuan NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(255) NOT NULL,
    FOREIGN KEY (ChuQuanId) REFERENCES NguoiDung(NguoiDungId)
);

CREATE TABLE DanhMuc (
    DanhMucId INT IDENTITY(1,1) PRIMARY KEY,
    TenDanhMuc NVARCHAR(100) NOT NULL
);

CREATE TABLE SanPham (
    SanPhamId CHAR(7) PRIMARY KEY,
    QuanAnId CHAR(7) NOT NULL,
    DanhMucId INT NOT NULL,
    TenSanPham NVARCHAR(100) NOT NULL,
    HinhAnh NVARCHAR(500),
    Gia DECIMAL(12,2) CHECK (Gia >= 0) NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT N'CÒN HÀNG',
    FOREIGN KEY (QuanAnId) REFERENCES QuanAn(QuanAnId),
    FOREIGN KEY (DanhMucId) REFERENCES DanhMuc(DanhMucId)
);

CREATE TABLE GioHang (
    GioHangId CHAR(7) PRIMARY KEY,
    KhachHangId CHAR(7) UNIQUE NOT NULL,
    QuanAnId CHAR(7) NOT NULL,
    FOREIGN KEY (KhachHangId) REFERENCES NguoiDung(NguoiDungId),
    FOREIGN KEY (QuanAnId) REFERENCES QuanAn(QuanAnId)
);

CREATE TABLE ChiTietGioHang (
    ChiTietGioHangId INT IDENTITY(1,1) PRIMARY KEY,
    GioHangId CHAR(7) NOT NULL,
    SanPhamId CHAR(7) NOT NULL,
    SoLuong INT CHECK (SoLuong > 0) NOT NULL,
    CONSTRAINT UQ_GioHang_SanPham UNIQUE (GioHangId, SanPhamId),
    FOREIGN KEY (GioHangId) REFERENCES GioHang(GioHangId) ON DELETE CASCADE,
    FOREIGN KEY (SanPhamId) REFERENCES SanPham(SanPhamId)
);

CREATE TABLE DonHang (
    DonHangId CHAR(7) PRIMARY KEY,
    KhachHangId CHAR(7) NOT NULL,
    QuanAnId CHAR(7) NOT NULL,
    DiaChiGiao NVARCHAR(255) NOT NULL,
    TongTien DECIMAL(14,2) CHECK (TongTien >= 0) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'CHỜ XÁC NHẬN',
    ThoiGianDat DATETIME DEFAULT GETDATE(),
    PhuongThucThanhToan NVARCHAR(50) DEFAULT N'Tiền mặt',
    TrangThaiThanhToan NVARCHAR(50) DEFAULT N'CHƯA THANH TOÁN',
    FOREIGN KEY (KhachHangId) REFERENCES NguoiDung(NguoiDungId),
    FOREIGN KEY (QuanAnId) REFERENCES QuanAn(QuanAnId)
);

CREATE TABLE ChiTietDonHang (
    ChiTietId INT IDENTITY(1,1) PRIMARY KEY,
    DonHangId CHAR(7) NOT NULL,
    SanPhamId CHAR(7) NOT NULL,
    SoLuong INT CHECK (SoLuong > 0) NOT NULL,
    GiaLucDat DECIMAL(12,2) CHECK (GiaLucDat >= 0) NOT NULL,
    CONSTRAINT UQ_DonHang_SanPham UNIQUE (DonHangId, SanPhamId),
    FOREIGN KEY (DonHangId) REFERENCES DonHang(DonHangId) ON DELETE CASCADE,
    FOREIGN KEY (SanPhamId) REFERENCES SanPham(SanPhamId)
);

CREATE TABLE GiaoHang (
    GiaoHangId CHAR(7) PRIMARY KEY,
    DonHangId CHAR(7) UNIQUE NOT NULL,
    ShipperId CHAR(7) NOT NULL,
    TrangThaiGiao NVARCHAR(50) DEFAULT N'ĐANG GIAO',
    ThoiGianNhan DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (DonHangId) REFERENCES DonHang(DonHangId),
    FOREIGN KEY (ShipperId) REFERENCES NguoiDung(NguoiDungId)
);

CREATE TABLE ThanhToan (
    ThanhToanId CHAR(7) PRIMARY KEY,
    DonHangId CHAR(7) UNIQUE NOT NULL,
    PhuongThuc NVARCHAR(50) DEFAULT N'Tiền mặt',
    SoTien DECIMAL(14,2) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'CHỜ THANH TOÁN',
    FOREIGN KEY (DonHangId) REFERENCES DonHang(DonHangId)
);

CREATE TABLE DanhGia (
    DanhGiaId VARCHAR(20) PRIMARY KEY,
    DonHangId CHAR(7) NOT NULL,
    KhachHangId CHAR(7) NOT NULL,
    QuanAnId CHAR(7) NOT NULL,
    SoSao INT CHECK (SoSao BETWEEN 1 AND 5) NOT NULL,
    NoiDung NVARCHAR(500),
    ThoiGianDanhGia DATETIME DEFAULT GETDATE(),
    CONSTRAINT UQ_DanhGia_DonHang_KhachHang UNIQUE (DonHangId, KhachHangId),
    FOREIGN KEY (DonHangId) REFERENCES DonHang(DonHangId),
    FOREIGN KEY (KhachHangId) REFERENCES NguoiDung(NguoiDungId),
    FOREIGN KEY (QuanAnId) REFERENCES QuanAn(QuanAnId)
);

-- ==========================================
-- PHAN 2: CHI MUC
-- ==========================================
CREATE INDEX IDX_NguoiDung_Email ON NguoiDung(Email);
CREATE INDEX IDX_DonHang_KhachHang ON DonHang(KhachHangId);
CREATE INDEX IDX_DonHang_QuanAn ON DonHang(QuanAnId);
CREATE INDEX IDX_SanPham_QuanAn ON SanPham(QuanAnId);
GO

-- ==========================================
-- PHAN 3: STORED PROCEDURES
-- ==========================================

CREATE PROCEDURE sp_LayNguoiDungTheoEmail
    @Email NVARCHAR(100)
AS
BEGIN
    SELECT NguoiDungId, RoleId, HoTen, MatKhau, TrangThai
    FROM NguoiDung
    WHERE Email = @Email AND TrangThai = N'HOẠT ĐỘNG';
END
GO

CREATE PROCEDURE sp_ThemGioHang
    @GioHangId CHAR(7),
    @SanPhamId CHAR(7),
    @SoLuongThem INT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM ChiTietGioHang WHERE GioHangId = @GioHangId AND SanPhamId = @SanPhamId)
    BEGIN
        UPDATE ChiTietGioHang
        SET SoLuong = SoLuong + @SoLuongThem
        WHERE GioHangId = @GioHangId AND SanPhamId = @SanPhamId;
    END
    ELSE
    BEGIN
        INSERT INTO ChiTietGioHang (GioHangId, SanPhamId, SoLuong)
        VALUES (@GioHangId, @SanPhamId, @SoLuongThem);
    END
END
GO

CREATE PROCEDURE sp_TaoDonHang
    @DonHangId CHAR(7),
    @KhachHangId CHAR(7),
    @GioHangId CHAR(7),
    @DiaChiGiao NVARCHAR(255),
    @PhuongThucThanhToan NVARCHAR(50) = N'Tiền mặt',
    @TrangThaiThanhToan NVARCHAR(50) = N'CHƯA THANH TOÁN'
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @QuanAnId CHAR(7);
        DECLARE @TongTien DECIMAL(14,2);

        SELECT @QuanAnId = QuanAnId
        FROM GioHang
        WHERE GioHangId = @GioHangId AND KhachHangId = @KhachHangId;

        SELECT @TongTien = SUM(ct.SoLuong * sp.Gia)
        FROM ChiTietGioHang ct
        JOIN SanPham sp ON ct.SanPhamId = sp.SanPhamId
        WHERE ct.GioHangId = @GioHangId;

        IF @QuanAnId IS NULL OR @TongTien IS NULL OR @TongTien <= 0
        BEGIN
            THROW 50001, N'Giỏ hàng không hợp lệ hoặc đang trống.', 1;
        END

        INSERT INTO DonHang (DonHangId, KhachHangId, QuanAnId, DiaChiGiao, TongTien, TrangThai, PhuongThucThanhToan, TrangThaiThanhToan)
        VALUES (@DonHangId, @KhachHangId, @QuanAnId, @DiaChiGiao, @TongTien, N'CHỜ XÁC NHẬN', @PhuongThucThanhToan, @TrangThaiThanhToan);

        INSERT INTO ThanhToan (ThanhToanId, DonHangId, PhuongThuc, SoTien, TrangThai)
        VALUES ('TT' + SUBSTRING(@DonHangId, 3, 5), @DonHangId, @PhuongThucThanhToan, @TongTien, @TrangThaiThanhToan);

        INSERT INTO ChiTietDonHang (DonHangId, SanPhamId, SoLuong, GiaLucDat)
        SELECT @DonHangId, ct.SanPhamId, ct.SoLuong, sp.Gia
        FROM ChiTietGioHang ct
        JOIN SanPham sp ON ct.SanPhamId = sp.SanPhamId
        WHERE ct.GioHangId = @GioHangId;

        DELETE FROM ChiTietGioHang WHERE GioHangId = @GioHangId;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE PROCEDURE sp_ShipperNhanDon
    @GiaoHangId CHAR(7),
    @DonHangId CHAR(7),
    @ShipperId CHAR(7)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION;

        INSERT INTO GiaoHang (GiaoHangId, DonHangId, ShipperId, TrangThaiGiao)
        VALUES (@GiaoHangId, @DonHangId, @ShipperId, N'ĐANG GIAO');

        UPDATE DonHang
        SET TrangThai = N'ĐANG GIAO'
        WHERE DonHangId = @DonHangId AND TrangThai = N'ĐANG CHUẨN BỊ';

        IF @@ROWCOUNT = 0
        BEGIN
            THROW 50002, N'Đơn hàng không ở trạng thái có thể giao.', 1;
        END

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
GO

CREATE PROCEDURE sp_CapNhatTrangThaiDon
    @DonHangId CHAR(7),
    @TrangThaiMoi NVARCHAR(50)
AS
BEGIN
    UPDATE DonHang
    SET TrangThai = @TrangThaiMoi,
        TrangThaiThanhToan = CASE
            WHEN @TrangThaiMoi = N'HOÀN THÀNH' AND PhuongThucThanhToan = N'Tiền mặt' THEN N'ĐÃ THANH TOÁN'
            WHEN @TrangThaiMoi = N'ĐÃ HỦY' AND TrangThaiThanhToan = N'ĐÃ THANH TOÁN' THEN N'ĐÃ HOÀN TIỀN'
            ELSE TrangThaiThanhToan
        END
    WHERE DonHangId = @DonHangId;

    UPDATE ThanhToan
    SET TrangThai = CASE
            WHEN @TrangThaiMoi = N'HOÀN THÀNH' AND PhuongThuc = N'Tiền mặt' THEN N'ĐÃ THANH TOÁN'
            WHEN @TrangThaiMoi = N'ĐÃ HỦY' AND TrangThai = N'ĐÃ THANH TOÁN' THEN N'ĐÃ HOÀN TIỀN'
            ELSE TrangThai
        END
    WHERE DonHangId = @DonHangId;
END
GO

-- ==========================================
-- PHAN 4: DU LIEU MAU
-- ==========================================

INSERT INTO Role (TenRole) VALUES
('ADMIN'),
('CUSTOMER'),
('SHIPPER'),
('RESTAURANT');

INSERT INTO NguoiDung VALUES
('ND00001',1,N'Admin Tổng','admin@foodapp.com','0905000001','123456',N'HOẠT ĐỘNG'),
('ND00002',2,N'Nguyễn Văn An','an@gmail.com','0905000002','123456',N'HOẠT ĐỘNG'),
('ND00003',2,N'Lê Thị Bình','binh@gmail.com','0905000003','123456',N'HOẠT ĐỘNG'),
('ND00004',2,N'Trần Văn Cường','cuong@gmail.com','0905000004','123456',N'HOẠT ĐỘNG'),
('ND00005',3,N'Shipper Hùng','ship1@gmail.com','0905000005','123456',N'HOẠT ĐỘNG'),
('ND00006',3,N'Shipper Tùng','ship2@gmail.com','0905000006','123456',N'HOẠT ĐỘNG'),
('ND00007',4,N'Quán Cơm Tấm','comtam@gmail.com','0905000007','123456',N'HOẠT ĐỘNG'),
('ND00008',4,N'Quán Trà Sữa','trasua@gmail.com','0905000008','123456',N'HOẠT ĐỘNG');

INSERT INTO QuanAn VALUES
('QA00001','ND00007',N'Cơm tấm Sài Gòn',N'12 Nguyễn Văn Linh, Đà Nẵng'),
('QA00002','ND00008',N'Trà sữa Chill',N'89 Lê Duẩn, Đà Nẵng');

INSERT INTO DanhMuc (TenDanhMuc) VALUES
(N'Cơm'),
(N'Bún/Phở'),
(N'Trà sữa'),
(N'Nước ép'),
(N'Ăn vặt'),
(N'Fast Food');

INSERT INTO SanPham VALUES
('SP00001','QA00001',1,N'Cơm sườn',NULL,35000,N'CÒN HÀNG'),
('SP00002','QA00001',1,N'Cơm gà',NULL,40000,N'CÒN HÀNG'),
('SP00003','QA00001',2,N'Bún bò',NULL,45000,N'CÒN HÀNG'),
('SP00004','QA00001',5,N'Chả giò',NULL,25000,N'CÒN HÀNG'),
('SP00005','QA00002',3,N'Trà sữa truyền thống',NULL,30000,N'CÒN HÀNG'),
('SP00006','QA00002',3,N'Trà sữa matcha',NULL,32000,N'CÒN HÀNG'),
('SP00007','QA00002',4,N'Nước ép cam',NULL,28000,N'CÒN HÀNG'),
('SP00008','QA00002',6,N'Khoai tây chiên',NULL,30000,N'CÒN HÀNG');

INSERT INTO GioHang VALUES
('GH00001','ND00002','QA00001'),
('GH00002','ND00003','QA00002');

INSERT INTO ChiTietGioHang (GioHangId, SanPhamId, SoLuong) VALUES
('GH00001','SP00001',2),
('GH00001','SP00003',1),
('GH00002','SP00005',1),
('GH00002','SP00008',2);

INSERT INTO DonHang (DonHangId, KhachHangId, QuanAnId, DiaChiGiao, TongTien, TrangThai, ThoiGianDat, PhuongThucThanhToan, TrangThaiThanhToan) VALUES
('DH00001','ND00002','QA00001',N'12 Hoàng Diệu',80000,N'CHỜ XÁC NHẬN',GETDATE()-1,N'Tiền mặt',N'CHƯA THANH TOÁN'),
('DH00002','ND00003','QA00002',N'89 Lý Thường Kiệt',60000,N'ĐANG GIAO',GETDATE(),N'Tiền mặt',N'CHƯA THANH TOÁN'),
('DH00003','ND00002','QA00001',N'12 Hoàng Diệu',35000,N'HOÀN THÀNH',GETDATE()-2,N'Chuyển khoản',N'ĐÃ THANH TOÁN'),
('DH00004','ND00003','QA00002',N'89 Lý Thường Kiệt',32000,N'ĐÃ HỦY',GETDATE()-3,N'Tiền mặt',N'THẤT BẠI');

INSERT INTO ChiTietDonHang (DonHangId, SanPhamId, SoLuong, GiaLucDat) VALUES
('DH00001','SP00001',2,35000),
('DH00001','SP00003',1,45000),
('DH00002','SP00005',1,30000),
('DH00002','SP00008',1,30000),
('DH00003','SP00001',1,35000),
('DH00004','SP00006',1,32000);

INSERT INTO GiaoHang VALUES
('GHG0001','DH00002','ND00005',N'ĐANG GIAO',GETDATE()),
('GHG0002','DH00003','ND00006',N'HOÀN THÀNH',GETDATE()-2);

INSERT INTO ThanhToan VALUES
('TT00001','DH00001',N'Tiền mặt',80000,N'CHƯA THANH TOÁN'),
('TT00002','DH00002',N'Tiền mặt',60000,N'CHƯA THANH TOÁN'),
('TT00003','DH00003',N'Chuyển khoản',35000,N'ĐÃ THANH TOÁN'),
('TT00004','DH00004',N'Tiền mặt',32000,N'THẤT BẠI');

INSERT INTO DanhGia (DanhGiaId, DonHangId, KhachHangId, QuanAnId, SoSao, NoiDung, ThoiGianDanhGia) VALUES
('DG00001','DH00003','ND00002','QA00001',5,N'Rất ngon',GETDATE()-2);
GO