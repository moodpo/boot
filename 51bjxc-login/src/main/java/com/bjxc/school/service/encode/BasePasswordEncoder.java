/*
 * 产          品:  DEP业务基础平台
 * 文   件  名:  BasePasswordEncoder.java
 * 版          权:  深圳市迪博企业管理技术有限公司 Copyright 2011-2020,  All rights reserved
 * 描          述:  DEP业务基础平台是深圳迪博企业风险管理技术有限公司自主研发的业务基础平台。是面向
 *            业务应用的管理软件开发平台。帮助软件开发人员突破技术瓶颈，实现少写源代码或
 *            不写源代码、快速地开发应用软件的目的。
 * 创   建  人:  wenbing.zhang
 * 创建时间:  2012-5-23
 */
package com.bjxc.school.service.encode;

/**
 * <p>Convenience base for all password encoders.</p>
 *
 * @author Ben Alex
 * @version $Id: BasePasswordEncoder.java 2217 2007-10-27 00:45:30Z luke_t $
 */
public abstract class BasePasswordEncoder implements PasswordEncoder {
    //~ Methods ========================================================================================================

    /**
     * Used by subclasses to extract the password and salt from a merged <code>String</code> created using
     * {@link #mergePasswordAndSalt(String,Object,boolean)}.<p>The first element in the returned array is the
     * password. The second element is the salt. The salt array element will always be present, even if no salt was
     * found in the <code>mergedPasswordSalt</code> argument.</p>
     *
     * @param mergedPasswordSalt as generated by <code>mergePasswordAndSalt</code>
     *
     * @return an array, in which the first element is the password and the second the salt
     *
     * @throws IllegalArgumentException if mergedPasswordSalt is null or empty.
     */
    protected String[] demergePasswordAndSalt(String mergedPasswordSalt) {
        if ((mergedPasswordSalt == null) || "".equals(mergedPasswordSalt)) {
            throw new IllegalArgumentException("Cannot pass a null or empty String");
        }

        String password = mergedPasswordSalt;
        String salt = "";

        int saltBegins = mergedPasswordSalt.lastIndexOf("{");

        if ((saltBegins != -1) && ((saltBegins + 1) < mergedPasswordSalt.length())) {
            salt = mergedPasswordSalt.substring(saltBegins + 1, mergedPasswordSalt.length() - 1);
            password = mergedPasswordSalt.substring(0, saltBegins);
        }

        return new String[] {password, salt};
    }

    /**
     * Used by subclasses to generate a merged password and salt <code>String</code>.<P>The generated password
     * will be in the form of <code>password{salt}</code>.</p>
     *  <p>A <code>null</code> can be passed to either method, and will be handled correctly. If the
     * <code>salt</code> is <code>null</code> or empty, the resulting generated password will simply be the passed
     * <code>password</code>. The <code>toString</code> method of the <code>salt</code> will be used to represent the
     * salt.</p>
     *
     * @param password the password to be used (can be <code>null</code>)
     * @param salt the salt to be used (can be <code>null</code>)
     * @param strict ensures salt doesn't contain the delimiters
     *
     * @return a merged password and salt <code>String</code>
     *
     * @throws IllegalArgumentException if the salt contains '{' or '}' characters.
     */
    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }

        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
}
