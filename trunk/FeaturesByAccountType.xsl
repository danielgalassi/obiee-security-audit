<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<html>
			<head>
				<style type="text/css">
		h1 {font-family: Helvetica, sans-serif; font-weight: bold; font-size: 24pt; color: #676767;}
		h2 {font-family: Helvetica, sans-serif; font-weight: bold; font-size: 20pt; color: #777777;}
		h3 {font-family: Helvetica, sans-serif; font-size: 14pt; color: #777777;}
		h4 {font-family: Helvetica, sans-serif; font-size: 14pt; color: #888888;}
		li {font-family: Helvetica, sans-serif; font-size: 10pt; color: #474747;}
		a  {font-family: Helvetica, sans-serif; font-size: 10pt; color: #444444;}
		*  {font-family: Helvetica, sans-serif; font-size: 8pt; color: #333333;}
		tr {height:30; font-size: 8.5pt;}
		tr:hover {background: rgb(248,248,248);}
		table {
			border-spacing: 0 0;
			margin: 1px;
			border-right: 1px solid #DEDEDE;
			font-family: Helvetica, sans-serif; font-size: 8.5pt;}
		thead th {
			font-family: Helvetica, sans-serif; font-size: 8pt;
			background: #EFEFEF;
			border-left: 1px solid #DEDEDE;
			border-top: 1px solid #DEDEDE;}
		tbody td {
        		font-family: Helvetica, sans-serif; font-size: 8.5pt;
			border-bottom: 1px solid #DEDEDE;
			border-left: 1px solid #DEDEDE;}
				</style>

				<title>Oracle Business Intelligence Security Matrix - Features</title>
			</head>
			<body>
				<!-- Security Matrix heading -->
				<h1>OBIEE Security Setup
		<span style="font-size: 24px; color: #676767; -moz-transform: scaleX(-1); -o-transform: scaleX(-1); -webkit-transform: scaleX(-1); transform: scaleX(-1); display: inline-block;">
			&#169;
		</span>
				</h1>
				<br/>
				<xsl:for-each select="/WebCatalog">
					<h2>Feature Privilege Matrix</h2>
					<br/>
					<!-- Matrix Section -->
					<table>
						<tbody>
							<!--thead>
								<tr>
									<td width="300px" style="text-align:center; font-family: Helvetica, sans-serif; font-size: 8pt; border-top: 1px solid #EFEFEF; border-left: 1px solid #EFEFEF; border-bottom: 1px solid #E3E3E3; background-color: rgb(250,250,250);">Privilege &amp; Groups / Account Types (right)</td-->
									<!-- Account Type list -->
									<!--xsl:for-each select="..//RoleType">
										<th width="150px" style="background: #EFEFEF; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
											<xsl:value-of select="@Name"/>
										</th>
									</xsl:for-each>
								</tr>
							</thead-->
							<!-- Component list and ticks -->
							<!-- Component Names -->
							<xsl:for-each select="./ComponentList/Component">
								<tr>
									<td style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
										<xsl:value-of select="@ComponentName"/>

										<!-- greyed out cells -->
										<xsl:for-each select="../../..//RoleType">
											<td width="150px" style="text-align:center; background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
												<xsl:value-of select="@Name"/>
											</td>
										</xsl:for-each>

									</td>
								</tr>
								<!-- Privilege Section -->
								<xsl:for-each select="./Privilege">
									<tr>
										<!-- Privilege Names -->
										<td style="background: #F6F6F6; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:20px;">
											<xsl:value-of select="@PrivilegeName"/>
										</td>

										<!-- greyed out cells -->
										<!--xsl:for-each select="../../..//RoleType">
											<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; text-align:center;">
												<xsl:value-of select="@Name"/>
											</td>
										</xsl:for-each-->
										<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; text-align:center;">
										<xsl:for-each select="./RoleList/Role">
										<xsl:value-of select="."/>
										</xsl:for-each>
										</td>
									</tr>
								</xsl:for-each>
							</xsl:for-each>
						</tbody>
					</table>

					<br/>
					<br/>
					<hr style="height: 1px; border: 0; background-color: #AAAAAA; width: 70%;"/>

				</xsl:for-each>
				<div style="padding-top: 20px;">
					<p style="color: rgb(128, 128, 128); float: left;">Mozilla Firefox&#8482; or Google Chrome&#169; are strongly recommended for best results.</p>
					<p style="color: rgb(128, 128, 128); float: right;">Generated using <a href="http://code.google.com/p/obiee-security-audit/" style="font-family: Helvetica, sans-serif; font-size: 8pt;color: rgb(128, 128, 128);" target="_blank">OBIEE Security Audit</a> application.</p>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
