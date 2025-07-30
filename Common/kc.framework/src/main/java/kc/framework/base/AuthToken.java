package kc.framework.base;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthToken {
	private String id;

	private String tenant;

	private boolean internal;

	private String domainName;

	private String containerName;

	private String encryptionKey;
}
