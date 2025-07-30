package kc.service.constants;

public class TimeOutConstants {
	/// <summary>
    /// Cookie：20分钟过期
    /// </summary>
    public final static int CookieTimeOut = 20; //Cookie：20分钟过期
    /// <summary>
    /// Cache：12小时过期
    /// </summary>
    public final static int CacheTimeOut = 12 * 60; //Cache：12小时过期
    /// <summary>
    /// Cache：5分钟过期
    /// </summary>
    public final static int CacheShortTimeOut = 5; //Cache：5分钟过期
    /// <summary>
    /// AccessToken：60分钟过期
    /// </summary>
    public final static int AccessTokenTimeOut = 60; //AccessToken：60分钟过期
    /// <summary>
    /// 短信验证码有效期：5分钟
    /// </summary>
    public final static int PhoneCodeTimeout = 5;//短信验证码有效期
    /// <summary>
    /// 默认缓存有效期：30天
    /// </summary>
    public final static int DefaultCacheTimeOut = 30 * 24 * 60; //Cache：30天
    /// <summary>
    /// 共用缓存有效期：15分钟
    /// </summary>
    public final static int SharedCacheTimeOut = 15; //Cache：15分钟

    /// <summary>
    /// 微信Toke过期时间：120分钟过期
    /// </summary>
    public final static int WeixTokenTimeOut = 120;
}
