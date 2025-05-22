package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.UserRoleDto;
import com.buihien.datn.repository.*;
import com.buihien.datn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SetupDataServiceImpl implements SetupDataService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private EthnicsRepository ethnicsRepository;
    @Autowired
    private ReligionRepository religionRepository;
    @Autowired
    private ProfessionRepository professionRepository;
    @Autowired
    private FamilyRelationshipRepository familyRelationshipRepository;
    @Autowired
    private BankRepository bankRepository;

    @Override
    public void setupRoles() {
        this.createRolesIfNotExists();
        this.createUserAdmin();
    }

    @Override
    public void setupNationality() {
        this.createNationalityIfNotExists();
    }

    @Override
    public void setupEthnics() {
        this.createEthnicsIfNotExists();
    }

    @Override
    public void setupReligion() {
        this.createReligionIfNotExists();
    }

    @Override
    public void setupProfession() {
        this.createProfessionsIfNotExists();
    }

    @Override
    public void setupFamilyRelationship() {
        this.createFamilyRelationshipsIfNotExists();
    }

    @Override
    public void setupBank() {
        this.createBankIfNotExists();
    }

    private void createRolesIfNotExists() {
        Map<String, String> roles = new HashMap<>();
        roles.put(DatnConstants.ROLE_ADMIN, "Quản trị viên toàn quyền");
        roles.put(DatnConstants.ROLE_SUPER_ADMIN, "Quản trị viên cấp cao");
        roles.put(DatnConstants.ROLE_USER, "Người dùng thông thường");
        roles.put(DatnConstants.ROLE_MANAGER, "Quản lý chung");
        roles.put(DatnConstants.ROLE_HR, "Nhân sự");
        roles.put(DatnConstants.ROLE_ACCOUNTANT, "Kế toán");
        roles.put(DatnConstants.ROLE_PROJECT_MANAGER, "Quản lý dự án");
        roles.put(DatnConstants.ROLE_EMPLOYEE, "Nhân viên");
        roles.put(DatnConstants.ROLE_AUDITOR, "Kiểm toán viên");
        roles.put(DatnConstants.ROLE_SUPPORT, "Hỗ trợ kỹ thuật");
        roles.put(DatnConstants.ROLE_IT, "Công nghệ thông tin");

        for (Map.Entry<String, String> entry : roles.entrySet()) {
            String roleName = entry.getKey();
            String description = entry.getValue();

            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(description);
                roleRepository.save(role);
            }
        }
    }

    private void createUserAdmin() {
        User user = userService.findUserByRole(DatnConstants.ROLE_ADMIN);
        if (user != null) return;
        Role role = roleRepository.findByName(DatnConstants.ROLE_ADMIN).orElse(null);
        UserDto userDto = new UserDto();
        userDto.setUsername("admin");
        userDto.setPassword("123456");
        userDto.setConfirmPassword("123456");
        userDto.setEmail("admin@gmail.com");
        UserRoleDto userRole = new UserRoleDto(role);
        if (userDto.getRoles() == null) {
            userDto.setRoles(new ArrayList<>());
        }
        userDto.getRoles().add(userRole);
        userService.saveOrUpdate(userDto);
    }

    private void createNationalityIfNotExists() {
        Map<String, String[]> nationalities = new HashMap<>();

        // Đông Nam Á
        nationalities.put("VN", new String[]{"Việt Nam", "Quốc gia Đông Nam Á, thủ đô Hà Nội"});
        nationalities.put("TH", new String[]{"Thái Lan", "Đất nước chùa vàng, nổi tiếng du lịch"});
        nationalities.put("ID", new String[]{"Indonesia", "Quốc gia quần đảo lớn nhất Đông Nam Á"});
        nationalities.put("MY", new String[]{"Malaysia", "Quốc gia đa sắc tộc Đông Nam Á"});
        nationalities.put("SG", new String[]{"Singapore", "Đảo quốc phát triển tại Đông Nam Á"});
        nationalities.put("PH", new String[]{"Philippines", "Quốc gia quần đảo nói tiếng Anh nhiều"});
        nationalities.put("MM", new String[]{"Myanmar", "Quốc gia đang chuyển đổi chính trị"});
        nationalities.put("KH", new String[]{"Campuchia", "Quốc gia có di sản Angkor Wat"});
        nationalities.put("LA", new String[]{"Lào", "Quốc gia không giáp biển ở Đông Nam Á"});
        nationalities.put("BN", new String[]{"Brunei", "Quốc gia nhỏ giàu tài nguyên dầu mỏ"});

        // Các nước lớn (G7, G20)
        nationalities.put("US", new String[]{"Hoa Kỳ", "Cường quốc kinh tế và quân sự thế giới"});
        nationalities.put("CN", new String[]{"Trung Quốc", "Nước đông dân nhất thế giới"});
        nationalities.put("JP", new String[]{"Nhật Bản", "Nền kinh tế lớn, công nghệ phát triển"});
        nationalities.put("DE", new String[]{"Đức", "Đầu tàu kinh tế châu Âu"});
        nationalities.put("FR", new String[]{"Pháp", "Trung tâm văn hóa và nghệ thuật châu Âu"});
        nationalities.put("GB", new String[]{"Vương quốc Anh", "Quốc gia có ảnh hưởng lịch sử"});
        nationalities.put("IT", new String[]{"Ý", "Đất nước nghệ thuật và thời trang"});
        nationalities.put("CA", new String[]{"Canada", "Đất nước rộng lớn, phát triển, đa văn hóa"});
        nationalities.put("RU", new String[]{"Nga", "Quốc gia lớn nhất thế giới về diện tích"});
        nationalities.put("BR", new String[]{"Brazil", "Quốc gia lớn nhất Nam Mỹ"});
        nationalities.put("IN", new String[]{"Ấn Độ", "Đông dân thứ hai, đang phát triển nhanh"});
        nationalities.put("AU", new String[]{"Úc", "Quốc gia châu Đại Dương, thiên nhiên phong phú"});
        nationalities.put("KR", new String[]{"Hàn Quốc", "Quốc gia phát triển tại Đông Á"});
        nationalities.put("MX", new String[]{"Mexico", "Đất nước Trung Mỹ với văn hóa đa dạng"});
        nationalities.put("SA", new String[]{"Ả Rập Xê Út", "Trung tâm dầu mỏ Trung Đông"});
        nationalities.put("ZA", new String[]{"Nam Phi", "Quốc gia phát triển nhất châu Phi"});

        // Các nước châu Á khác
        nationalities.put("PK", new String[]{"Pakistan", "Quốc gia Nam Á, đông dân"});
        nationalities.put("BD", new String[]{"Bangladesh", "Quốc gia đông dân, đang phát triển"});
        nationalities.put("IR", new String[]{"Iran", "Quốc gia Trung Đông với lịch sử lâu đời"});
        nationalities.put("IQ", new String[]{"Iraq", "Quốc gia Trung Đông nhiều biến động"});
        nationalities.put("TR", new String[]{"Thổ Nhĩ Kỳ", "Nằm giữa châu Âu và châu Á"});
        nationalities.put("IL", new String[]{"Israel", "Quốc gia công nghệ cao tại Trung Đông"});
        nationalities.put("AF", new String[]{"Afghanistan", "Quốc gia Nam Á"});
        nationalities.put("NP", new String[]{"Nepal", "Nơi có đỉnh Everest cao nhất thế giới"});
        nationalities.put("LK", new String[]{"Sri Lanka", "Hòn ngọc Ấn Độ Dương"});
        nationalities.put("KZ", new String[]{"Kazakhstan", "Quốc gia Trung Á lớn nhất"});
        nationalities.put("AE", new String[]{"UAE", "Liên bang Ả Rập thống nhất, phát triển giàu có"});
        nationalities.put("QA", new String[]{"Qatar", "Quốc gia nhỏ, giàu tài nguyên dầu mỏ"});
        nationalities.put("KW", new String[]{"Kuwait", "Quốc gia Trung Đông giàu có"});
        nationalities.put("OM", new String[]{"Oman", "Quốc gia Trung Đông yên bình"});

        List<Country> countries = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : nationalities.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (countryRepository.findByCode(code) == null) {
                Country country = new Country();
                country.setCode(code);
                country.setName(name);
                country.setDescription(description);
                countries.add(country);
            }
        }

        if (!countries.isEmpty()) {
            countryRepository.saveAll(countries);
        }
    }

    private void createEthnicsIfNotExists() {
        Map<String, String[]> ethnics = new LinkedHashMap<>();

        ethnics.put("KINH", new String[]{"Kinh", "Dân tộc chiếm đa số tại Việt Nam"});
        ethnics.put("TAY", new String[]{"Tày", "Dân tộc thiểu số lớn thứ hai ở Việt Nam"});
        ethnics.put("THAI", new String[]{"Thái", "Phân bố chủ yếu ở vùng Tây Bắc"});
        ethnics.put("MUONG", new String[]{"Mường", "Sống chủ yếu ở Hòa Bình, Thanh Hóa"});
        ethnics.put("KHMER", new String[]{"Khmer", "Phân bố chủ yếu ở miền Tây Nam Bộ"});
        ethnics.put("HOA", new String[]{"Hoa", "Người gốc Trung Quốc sinh sống tại Việt Nam"});
        ethnics.put("NUNG", new String[]{"Nùng", "Sống chủ yếu ở Cao Bằng, Lạng Sơn"});
        ethnics.put("HMONG", new String[]{"H'Mông", "Dân tộc miền núi phía Bắc"});
        ethnics.put("DAO", new String[]{"Dao", "Dân tộc miền núi, có trang phục đặc sắc"});
        ethnics.put("GIA_RAI", new String[]{"Gia Rai", "Dân tộc sống ở Tây Nguyên"});
        ethnics.put("EDE", new String[]{"Ê Đê", "Sinh sống tại Đắk Lắk"});
        ethnics.put("BA_NA", new String[]{"Ba Na", "Sinh sống ở Tây Nguyên"});
        ethnics.put("SAN_CHAY", new String[]{"Sán Chay", "Còn gọi là Cao Lan"});
        ethnics.put("CHAM", new String[]{"Chăm", "Có văn hóa và tôn giáo riêng"});
        ethnics.put("SAN_DIU", new String[]{"Sán Dìu", "Sống ở vùng trung du Bắc Bộ"});
        ethnics.put("HRERE", new String[]{"Hrê", "Sống tại Quảng Ngãi"});
        ethnics.put("RA_GLAI", new String[]{"Ra Glai", "Còn gọi là La Vang"});
        ethnics.put("XO_DANG", new String[]{"Xơ Đăng", "Tập trung ở Kon Tum"});
        ethnics.put("CO_HO", new String[]{"Cơ Ho", "Sinh sống ở Lâm Đồng"});
        ethnics.put("CHU_RU", new String[]{"Chu Ru", "Cùng ngữ hệ Môn-Khmer"});
        ethnics.put("BRAU", new String[]{"Brâu", "Dân tộc rất ít người ở Việt Nam"});
        ethnics.put("RO_MAM", new String[]{"Rơ Măm", "Dân tộc rất ít người"});
        ethnics.put("PU_PEO", new String[]{"Pà Thẻn", "Ít người, sống tại Hà Giang"});
        ethnics.put("LA_HU", new String[]{"Lahu", "Ít người, sống ở Điện Biên"});
        ethnics.put("KHANG", new String[]{"Kháng", "Sống ở Sơn La, Điện Biên"});
        ethnics.put("LO_LO", new String[]{"Lô Lô", "Sống tại vùng cực Bắc Hà Giang"});
        ethnics.put("PHU_LA", new String[]{"Phù Lá", "Còn gọi là Xá Phó"});
        ethnics.put("LA_CHI", new String[]{"La Chí", "Sống ở Hà Giang"});
        ethnics.put("CO_LAO", new String[]{"Cờ Lao", "Dân tộc ít người ở Hà Giang"});
        ethnics.put("MANG", new String[]{"Mảng", "Ít người, sống ở Lai Châu"});
        ethnics.put("CHO_RO", new String[]{"Chơ Ro", "Sống ở Đồng Nai"});
        ethnics.put("CHUT", new String[]{"Chứt", "Sống tại Quảng Bình"});
        ethnics.put("GIE_TRIENG", new String[]{"Gié Triêng", "Kon Tum và Quảng Nam"});
        ethnics.put("HA_NHI", new String[]{"Hà Nhì", "Sống ở biên giới Việt-Trung"});
        ethnics.put("LA_HA", new String[]{"La Ha", "Sống ở Sơn La"});
        ethnics.put("LU", new String[]{"Lự", "Sống ở Lai Châu"});
        ethnics.put("NGAI", new String[]{"Ngái", "Ít người, vùng đông Bắc"});
        ethnics.put("O_DU", new String[]{"Ơ Đu", "Rất ít người, ở Nghệ An"});
        ethnics.put("PA_THEN", new String[]{"Pà Thẻn", "Sống tại Hà Giang"});
        ethnics.put("SI_LA", new String[]{"Si La", "Sống tại Mường Tè, Lai Châu"});
        ethnics.put("TALAI", new String[]{"Tà Ôi", "Sinh sống tại Thừa Thiên Huế"});
        ethnics.put("TAI", new String[]{"Tà Mun", "Ít người, sống ở miền núi"});
        ethnics.put("XINH_MUN", new String[]{"Xinh Mun", "Sống ở Sơn La"});
        ethnics.put("XTIENG", new String[]{"X'tiêng", "Sống ở Bình Phước"});
        ethnics.put("CO_TU", new String[]{"Cơ Tu", "Sống ở miền Trung"});
        ethnics.put("CO", new String[]{"Cơ", "Sống ở Quảng Ngãi"});
        ethnics.put("GIAY", new String[]{"Giáy", "Sống tại Lào Cai"});
        ethnics.put("BO_Y", new String[]{"Bố Y", "Sống ở Lào Cai"});
        ethnics.put("LAU", new String[]{"Lào", "Dân tộc Lào tại Việt Nam"});
        ethnics.put("MA", new String[]{"Mạ", "Ít người, sống tại Lâm Đồng"});
        ethnics.put("MNONG", new String[]{"Mnông", "Sống ở Tây Nguyên"});
        ethnics.put("XO", new String[]{"Xơ", "Xơ Đăng, sống ở Kon Tum"});

        List<Ethnics> ethnicEntities = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : ethnics.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (ethnicsRepository.findByCode(code) == null) {
                Ethnics ethnic = new Ethnics();
                ethnic.setCode(code);
                ethnic.setName(name);
                ethnic.setDescription(description);
                ethnicEntities.add(ethnic);
            }
        }

        if (!ethnicEntities.isEmpty()) {
            ethnicsRepository.saveAll(ethnicEntities);
        }
    }

    private void createReligionIfNotExists() {
        Map<String, String[]> religions = new LinkedHashMap<>();

        religions.put("PHAT_GIAO", new String[]{"Phật giáo", "Tôn giáo lớn nhất tại Việt Nam, du nhập từ Ấn Độ"});
        religions.put("THIEN_CHUA", new String[]{"Thiên Chúa giáo", "Gồm Công giáo và Tin Lành, có mặt rộng rãi tại Việt Nam"});
        religions.put("CAO_DAI", new String[]{"Cao Đài", "Tôn giáo ra đời tại Việt Nam"});
        religions.put("HOA_HAO", new String[]{"Hòa Hảo", "Tôn giáo nội sinh của người Việt Nam"});
        religions.put("TIN_LANH", new String[]{"Tin Lành", "Nhánh của Thiên Chúa giáo, du nhập từ phương Tây"});
        religions.put("HINDU", new String[]{"Hindu giáo", "Tôn giáo cổ xưa của Ấn Độ"});
        religions.put("HOI_GIAO", new String[]{"Hồi giáo", "Tôn giáo lớn thứ hai thế giới, còn gọi là Islam"});
        religions.put("DO_GIAO", new String[]{"Đạo giáo", "Tôn giáo truyền thống của Trung Quốc"});
        religions.put("NHO_GIAO", new String[]{"Nho giáo", "Hệ tư tưởng và triết học Á Đông, ảnh hưởng sâu rộng tại Việt Nam"});
        religions.put("KHONG", new String[]{"Không", "Không theo tôn giáo nào cụ thể"});

        List<Religion> religionsEntity = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : religions.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (religionRepository.findByCode(code) == null) {
                Religion religion = new Religion();
                religion.setCode(code);
                religion.setName(name);
                religion.setDescription(description);
                religionsEntity.add(religion);
            }
        }

        if (!religionsEntity.isEmpty()) {
            religionRepository.saveAll(religionsEntity);
        }
    }

    private void createProfessionsIfNotExists() {
        Map<String, String[]> professions = new LinkedHashMap<>();

        professions.put("CONG_NHAN", new String[]{"Công nhân", "Người làm việc trong nhà máy, xí nghiệp"});
        professions.put("KY_SU", new String[]{"Kỹ sư", "Người thiết kế, xây dựng và giám sát kỹ thuật"});
        professions.put("BAC_SI", new String[]{"Bác sĩ", "Người khám và chữa bệnh"});
        professions.put("Y_TA", new String[]{"Y tá", "Người hỗ trợ chăm sóc bệnh nhân"});
        professions.put("GIAO_VIEN", new String[]{"Giáo viên", "Người giảng dạy trong trường học"});
        professions.put("GIANG_VIEN", new String[]{"Giảng viên", "Người giảng dạy tại trường đại học"});
        professions.put("LUAT_SU", new String[]{"Luật sư", "Người tư vấn và đại diện pháp lý"});
        professions.put("CONG_AN", new String[]{"Công an", "Người làm việc trong ngành an ninh"});
        professions.put("BO_DOI", new String[]{"Bộ đội", "Người làm việc trong quân đội"});
        professions.put("IT", new String[]{"Nhân viên IT", "Người làm việc trong lĩnh vực công nghệ thông tin"});
        professions.put("KE_TOAN", new String[]{"Kế toán", "Người quản lý tài chính, sổ sách"});
        professions.put("NHAN_VIEN_VAN_PHONG", new String[]{"Nhân viên văn phòng", "Người làm việc hành chính"});
        professions.put("KINH_DOANH", new String[]{"Nhân viên kinh doanh", "Người làm việc trong lĩnh vực bán hàng"});
        professions.put("TIEP_TAN", new String[]{"Lễ tân", "Người tiếp khách và hỗ trợ thông tin"});
        professions.put("NGHE_TU_DO", new String[]{"Nghề tự do", "Không thuộc tổ chức cụ thể, làm việc độc lập"});
        professions.put("SINH_VIEN", new String[]{"Sinh viên", "Người đang theo học tại đại học"});
        professions.put("HOC_SINH", new String[]{"Học sinh", "Người đang theo học phổ thông"});
        professions.put("VE_HUU", new String[]{"Về hưu", "Người đã nghỉ hưu"});
        professions.put("THAT_NGHIEP", new String[]{"Thất nghiệp", "Không có công việc ổn định"});
        professions.put("KHAC", new String[]{"Khác", "Nghề nghiệp khác không liệt kê"});

        List<Profession> professionEntities = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : professions.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (professionRepository.findByCode(code) == null) {
                Profession profession = new Profession();
                profession.setCode(code);
                profession.setName(name);
                profession.setDescription(description);
                professionEntities.add(profession);
            }
        }

        if (!professionEntities.isEmpty()) {
            professionRepository.saveAll(professionEntities);
        }
    }

    private void createFamilyRelationshipsIfNotExists() {
        Map<String, String[]> relationships = new LinkedHashMap<>();

        relationships.put("CHA", new String[]{"Cha", "Bố ruột"});
        relationships.put("ME", new String[]{"Mẹ", "Mẹ ruột"});
        relationships.put("VO", new String[]{"Vợ", "Người vợ hợp pháp"});
        relationships.put("CHONG", new String[]{"Chồng", "Người chồng hợp pháp"});
        relationships.put("CON_TRAI", new String[]{"Con trai", "Con ruột là nam"});
        relationships.put("CON_GAI", new String[]{"Con gái", "Con ruột là nữ"});
        relationships.put("ANH_TRAI", new String[]{"Anh trai", "Anh ruột"});
        relationships.put("EM_TRAI", new String[]{"Em trai", "Em ruột là nam"});
        relationships.put("CHI_GAI", new String[]{"Chị gái", "Chị ruột"});
        relationships.put("EM_GAI", new String[]{"Em gái", "Em ruột là nữ"});
        relationships.put("ONG_NOI", new String[]{"Ông nội", "Cha của cha"});
        relationships.put("BA_NOI", new String[]{"Bà nội", "Mẹ của cha"});
        relationships.put("ONG_NGOAI", new String[]{"Ông ngoại", "Cha của mẹ"});
        relationships.put("BA_NGOAI", new String[]{"Bà ngoại", "Mẹ của mẹ"});
        relationships.put("CO", new String[]{"Cô", "Chị/em gái của cha"});
        relationships.put("DI", new String[]{"Dì", "Chị/em gái của mẹ"});
        relationships.put("CHU", new String[]{"Chú", "Em trai của cha"});
        relationships.put("BAC", new String[]{"Bác", "Anh/chị của cha/mẹ"});
        relationships.put("CHAU", new String[]{"Cháu", "Cháu nội/ngoại hoặc cháu họ"});
        relationships.put("KHAC", new String[]{"Khác", "Quan hệ khác không liệt kê"});

        List<FamilyRelationship> relationshipEntities = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : relationships.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (familyRelationshipRepository.findByCode(code) == null) {
                FamilyRelationship relationship = new FamilyRelationship();
                relationship.setCode(code);
                relationship.setName(name);
                relationship.setDescription(description);
                relationshipEntities.add(relationship);
            }
        }

        if (!relationshipEntities.isEmpty()) {
            familyRelationshipRepository.saveAll(relationshipEntities);
        }
    }

    private void createBankIfNotExists() {
        Map<String, String[]> banks = new LinkedHashMap<>();

        banks.put("SHB", new String[]{"SHB", "Ngân hàng TMCP Sài Gòn - Hà Nội"});
        banks.put("EIB", new String[]{"Eximbank", "Ngân hàng TMCP Xuất Nhập Khẩu Việt Nam"});
        banks.put("OCB", new String[]{"OCB", "Ngân hàng TMCP Phương Đông"});
        banks.put("LPB", new String[]{"LienVietPostBank", "Ngân hàng TMCP Bưu điện Liên Việt"});
        banks.put("BAB", new String[]{"Bac A Bank", "Ngân hàng TMCP Bắc Á"});
        banks.put("PGB", new String[]{"PG Bank", "Ngân hàng TMCP Xăng dầu Petrolimex"});
        banks.put("TPB", new String[]{"TPBank", "Ngân hàng TMCP Tiên Phong"});
        banks.put("MSB", new String[]{"MSB", "Ngân hàng TMCP Hàng Hải Việt Nam"});
        banks.put("NAB", new String[]{"Nam A Bank", "Ngân hàng TMCP Nam Á"});
        banks.put("SEA", new String[]{"SeABank", "Ngân hàng TMCP Đông Nam Á"});
        banks.put("VAB", new String[]{"VietABank", "Ngân hàng TMCP Việt Á"});
        banks.put("KLB", new String[]{"KienlongBank", "Ngân hàng TMCP Kiên Long"});
        banks.put("SGB", new String[]{"Saigonbank", "Ngân hàng TMCP Sài Gòn Công Thương"});
        banks.put("ABB", new String[]{"ABBANK", "Ngân hàng TMCP An Bình"});
        banks.put("BVB", new String[]{"Viet Capital Bank", "Ngân hàng TMCP Bản Việt"});
        banks.put("IVB", new String[]{"Indovina Bank", "Ngân hàng TNHH Indovina"});
        banks.put("VRB", new String[]{"VRB", "Ngân hàng Liên doanh Việt – Nga"});
        banks.put("HSBC", new String[]{"HSBC", "Ngân hàng TNHH MTV HSBC Việt Nam"});
        banks.put("CITIBANK", new String[]{"Citibank", "Ngân hàng Citibank Việt Nam"});
        banks.put("UOB", new String[]{"UOB", "Ngân hàng United Overseas Bank Việt Nam"});
        banks.put("STANDARD", new String[]{"Standard Chartered", "Ngân hàng Standard Chartered Việt Nam"});
        banks.put("WOO", new String[]{"Woori Bank", "Ngân hàng TNHH MTV Woori Bank Việt Nam"});
        banks.put("KEB", new String[]{"KEB Hana Bank", "Ngân hàng TNHH MTV KEB Hana Bank tại Việt Nam"});
        banks.put("PUBLIC", new String[]{"Public Bank", "Ngân hàng Public Bank Việt Nam"});

        List<Bank> bankEntities = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : banks.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue()[0];
            String description = entry.getValue()[1];

            if (bankRepository.findByCode(code) == null) {
                Bank bank = new Bank();
                bank.setCode(code);
                bank.setName(name);
                bank.setDescription(description);
                bankEntities.add(bank);
            }
        }

        if (!bankEntities.isEmpty()) {
            bankRepository.saveAll(bankEntities);
        }
    }


}
