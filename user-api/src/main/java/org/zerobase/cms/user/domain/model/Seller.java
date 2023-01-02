package org.zerobase.cms.user.domain.model;

import org.zerobase.cms.user.domain.SignUpForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private String name;
	private String password;
	private String phone;
	private LocalDate birth;

	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;

	public static Seller from(SignUpForm form) {
		return Seller.builder()
			.email(form.getEmail())
			.name(form.getName())
			.password(form.getPassword())
			.birth(form.getBirth())
			.phone(form.getPhone())
			.verify(false)
			.build();
	}

}
