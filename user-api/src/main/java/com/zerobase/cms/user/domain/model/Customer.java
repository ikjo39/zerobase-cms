package com.zerobase.cms.user.domain.model;


import com.zerobase.cms.user.domain.SignUpForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;

	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;

	// 기본적인 validation 고민 해보기
	public static Customer from(SignUpForm form) {
		return Customer.builder()
			.email(form.getEmail().toLowerCase(Locale.ROOT))
			.password(form.getPassword())
			.name(form.getName())
			.birth(form.getBirth())
			.phone(form.getPhone())
			.verify(false)
			.build();
	}
}
