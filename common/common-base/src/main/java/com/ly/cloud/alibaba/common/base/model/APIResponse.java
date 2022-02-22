/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ly.cloud.alibaba.common.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 响应信息主体
 *
 * @author ly
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class APIResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private String msg;
	private String traceId;
	private T data;

	public APIResponse(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}

