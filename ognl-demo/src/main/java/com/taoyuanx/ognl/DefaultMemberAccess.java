//--------------------------------------------------------------------------
//	Copyright (c) 1998-2004, Drew Davidson and Luke Blanshard
//  All rights reserved.
//
//	Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are
//  met:
//
//	Redistributions of source code must retain the above copyright notice,
//  this list of conditions and the following disclaimer.
//	Redistributions in binary form must reproduce the above copyright
//  notice, this list of conditions and the following disclaimer in the
//  documentation and/or other materials provided with the distribution.
//	Neither the name of the Drew Davidson nor the names of its contributors
//  may be used to endorse or promote products derived from this software
//  without specific prior written permission.
//
//	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
//  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
//  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
//  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
//  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
//  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
//  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
//  AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
//  THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
//  DAMAGE.
//--------------------------------------------------------------------------
package com.taoyuanx.ognl;

import ognl.MemberAccess;

import java.lang.reflect.*;
import java.util.*;

public class DefaultMemberAccess implements MemberAccess
{
    public boolean      allowPrivateAccess = false;
    public boolean      allowProtectedAccess = false;
    public boolean      allowPackageProtectedAccess = false;

	/*===================================================================
		Constructors
	  ===================================================================*/
	public DefaultMemberAccess(boolean allowAllAccess)
	{
	    this(allowAllAccess, allowAllAccess, allowAllAccess);
	}

	public DefaultMemberAccess(boolean allowPrivateAccess, boolean allowProtectedAccess, boolean allowPackageProtectedAccess)
	{
	    super();
	    this.allowPrivateAccess = allowPrivateAccess;
	    this.allowProtectedAccess = allowProtectedAccess;
	    this.allowPackageProtectedAccess = allowPackageProtectedAccess;
	}

	/*===================================================================
		Public methods
	  ===================================================================*/
	public boolean getAllowPrivateAccess()
	{
	    return allowPrivateAccess;
	}

	public void setAllowPrivateAccess(boolean value)
	{
	    allowPrivateAccess = value;
	}

	public boolean getAllowProtectedAccess()
	{
	    return allowProtectedAccess;
	}

	public void setAllowProtectedAccess(boolean value)
	{
	    allowProtectedAccess = value;
	}

	public boolean getAllowPackageProtectedAccess()
	{
	    return allowPackageProtectedAccess;
	}

	public void setAllowPackageProtectedAccess(boolean value)
	{
	    allowPackageProtectedAccess = value;
	}


    @Override
	public Object setup(Map context, Object target, Member member, String propertyName)
    {
        Object      result = null;

        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject    accessible = (AccessibleObject)member;

            if (!accessible.isAccessible()) {
                result = Boolean.TRUE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }
	@Override
    public void restore(Map context, Object target, Member member, String propertyName, Object state)
    {
        if (state != null) {
            ((AccessibleObject)member).setAccessible(((Boolean)state).booleanValue());
        }
    }


	@Override
	public boolean isAccessible(Map context, Object target, Member member, String propertyName)
	{
	    int         modifiers = member.getModifiers();
	    boolean     result = Modifier.isPublic(modifiers);

	    if (!result) {
	        if (Modifier.isPrivate(modifiers)) {
	            result = getAllowPrivateAccess();
	        } else {
	            if (Modifier.isProtected(modifiers)) {
	                result = getAllowProtectedAccess();
	            } else {
	                result = getAllowPackageProtectedAccess();
	            }
	        }
	    }
	    return result;
	}
}
