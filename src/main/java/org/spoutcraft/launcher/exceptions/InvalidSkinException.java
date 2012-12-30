/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the Spout License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.launcher.exceptions;

public class InvalidSkinException extends Exception {
	private static final long serialVersionUID = 5907555277800661037L;
	private final Throwable cause;
	private final String message;

	public InvalidSkinException(String message, Throwable cause) {
		this.cause = cause;
		this.message = message;
	}

	public InvalidSkinException(Throwable cause) {
		this(null, cause);
	}

	public InvalidSkinException(String message) {
		this(message, null);
	}

	public InvalidSkinException() {
		this(null, null);
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return message;
	}
}