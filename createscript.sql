-- Database: dawAPI

-- DROP DATABASE "dawAPI";

create table Users (
	Username TEXT primary key,
	Password text not null
);

create table ChecklistTemplate (
	Id serial primary key,
	Name text not null,
	Usable boolean not null,
	Username text references Users not null
);

create table Checklist (
	Id serial primary key,
	Name text not null,
	CompletionDate text not null,
	Username text references Users not null, 
	checklisttemplate_id int references ChecklistTemplate
);

create table ChecklistItem (
	Id serial primary key,
	Name text not null,
	Description text not null,
	State text check (State = 'completed' or State = 'uncompleted'),
	Checklist_id int references Checklist not null
);

create table ChecklistTemplateItem (
	Id serial primary key,
	Name text not null,
	Description text not null,
	ChecklistTemplate_id int references ChecklistTemplate not null
);

insert into Users(Username, Password)
	values('Rui', 'rui'),
		  ('Steven', 'steven'),
		  ('Daniela', 'daniela');
		
insert into Checklist(Name, CompletionDate, Username)
	values('Lista 1', '2018-03-17T20:00:00+00:00', 'Rui'),
		  ('Lista 2', '2018-03-23', 'Steven'),
		  ('Lista 3', '2018-03-23', 'Daniela');
		
insert into ChecklistItem(Name, Description, State, Checklist_id)
	values('Item 1.1', '', 'uncompleted', 1),
		  ('Item 1.2', 'uncompleted item', 'uncompleted', 1);
		
insert into ChecklistTemplate(Name, Username, Usable)
	values('Template 1', 'Rui', true);
	
insert into ChecklistTemplateItem(Name, Description, ChecklistTemplate_id)
	values('Item T1.1', 'Item template 1', 1),
		  ('Item T1.2', 'Item template 2', 1);