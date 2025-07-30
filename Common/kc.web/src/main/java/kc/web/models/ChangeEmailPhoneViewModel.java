package kc.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailPhoneViewModel implements java.io.Serializable {

	private static final long serialVersionUID = -2840638638279720450L;

	public String Email;

	public String Phone;
}
